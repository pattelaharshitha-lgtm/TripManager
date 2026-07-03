// ============================================================
// MainFrame.java
// This is the MAIN file that builds the entire application.
// It creates the window, tabs, and all the input/output panels.
//
// HOW TO RUN:
//   - In Eclipse/NetBeans: Right-click → Run As → Java Application
//   - Make sure all 4 files are in the same folder/package
// ============================================================

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    // ============================================================
    // APPLICATION-LEVEL DATA (shared across all tabs)
    // ============================================================

    Profile userProfile = null;   // Stores the user's profile
    Trip currentTrip = null;      // Stores the active trip

    // Array to hold up to 100 expenses
    Expense[] expenses = new Expense[100];
    int expenseCount = 0;         // Tracks how many expenses added so far

    // ============================================================
    // TABS (each tab is a JPanel)
    // ============================================================

    JTabbedPane tabbedPane;

    // --- Tab 1: Profile ---
    JTextField profileNameField, profileAgeField;
    JLabel tripCountLabel;

    // --- Tab 2: Create Trip ---
    JTextField tripNameField;
    JRadioButton soloButton, groupButton;
    JPanel soloPanel, groupPanel;

    // Solo fields
    JTextField budgetField, tripDaysField, placesField, dailyExpField;

    // Group fields
    JTextField numPeopleField, groupBudgetField;
    JPanel memberInputPanel;
    JTextField[] memberNameFields;
    JTextField[] memberBudgetFields;
    int numPeople = 0;

    // --- Tab 3: Trip Details (labels to display info) ---
    JLabel detailProfile, detailTripName, detailTripType;
    JLabel detailBudget, detailSpent, detailRemaining;
    JTextArea detailMembersArea;

    // --- Tab 4: Expenses ---
    JTextField expPlaceField, expAmountField, expPersonField, expDateField;
    DefaultTableModel expenseTableModel;

    // --- Tab 5: Split Calculator ---
    JTextField splitTotalField, splitPeopleField;
    JTextArea splitResultArea;

    // ============================================================
    // CONSTRUCTOR: Sets up the whole window
    // ============================================================
    public MainFrame() {
        setTitle("Trip Money Management System");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Add all 5 tabs
        tabbedPane.addTab("👤 Profile", createProfileTab());
        tabbedPane.addTab("✈ Create Trip", createTripTab());
        tabbedPane.addTab("📋 Trip Details", createTripDetailsTab());
        tabbedPane.addTab("💸 Expenses", createExpensesTab());
        tabbedPane.addTab("🧮 Split Calculator", createSplitTab());

        add(tabbedPane);
        setVisible(true);
    }

    // ============================================================
    // TAB 1: PROFILE TAB
    // ============================================================
    private JPanel createProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(30, 100, 200));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // Name field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        profileNameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(profileNameField, gbc);

        // Age field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        profileAgeField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(profileAgeField, gbc);

        // Trip count display
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Trips Created:"), gbc);
        tripCountLabel = new JLabel("0");
        tripCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        panel.add(tripCountLabel, gbc);

        // Save Profile button
        JButton saveProfileBtn = new JButton("💾 Save Profile");
        styleButton(saveProfileBtn, new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(saveProfileBtn, gbc);

        // Create Trip button
        JButton createTripBtn = new JButton("✈ Create Trip");
        styleButton(createTripBtn, new Color(34, 139, 34));
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(createTripBtn, gbc);

        // ---- Button Actions ----

        // Save Profile: reads fields and creates Profile object
        saveProfileBtn.addActionListener(e -> {
            String name = profileNameField.getText().trim();
            String ageText = profileAgeField.getText().trim();

            if (name.isEmpty() || ageText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Name and Age!");
                return;
            }
            try {
                int age = Integer.parseInt(ageText);
                userProfile = new Profile(name, age);
                JOptionPane.showMessageDialog(this, "Profile saved! Welcome, " + name + "!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Age must be a number!");
            }
        });

        // Create Trip: switch to tab 2
        createTripBtn.addActionListener(e -> {
            if (userProfile == null) {
                JOptionPane.showMessageDialog(this, "Please save your profile first!");
                return;
            }
            tabbedPane.setSelectedIndex(1); // Go to Create Trip tab
        });

        return panel;
    }

    // ============================================================
    // TAB 2: CREATE TRIP TAB
    // ============================================================
    private JPanel createTripTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 255, 245));

        // --- TOP SECTION: Trip name + type selection ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(245, 255, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel title = new JLabel("Create a New Trip");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(34, 139, 34));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        topPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // Trip name
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Trip Name:"), gbc);
        tripNameField = new JTextField(22);
        gbc.gridx = 1;
        topPanel.add(tripNameField, gbc);

        // Radio buttons for trip type
        gbc.gridx = 0; gbc.gridy = 2;
        topPanel.add(new JLabel("Trip Type:"), gbc);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        radioPanel.setBackground(new Color(245, 255, 245));
        soloButton = new JRadioButton("Solo Trip");
        groupButton = new JRadioButton("Group Trip");
        soloButton.setBackground(new Color(245, 255, 245));
        groupButton.setBackground(new Color(245, 255, 245));

        // Group them so only one can be selected at a time
        ButtonGroup bg = new ButtonGroup();
        bg.add(soloButton);
        bg.add(groupButton);
        soloButton.setSelected(true); // Default: Solo

        radioPanel.add(soloButton);
        radioPanel.add(groupButton);
        gbc.gridx = 1;
        topPanel.add(radioPanel, gbc);

        panel.add(topPanel, BorderLayout.NORTH);

        // --- MIDDLE: Solo panel + Group panel (only one shows at a time) ---
        JPanel centerPanel = new JPanel(new CardLayout());
        centerPanel.setBackground(new Color(245, 255, 245));

        soloPanel = buildSoloPanel();
        groupPanel = buildGroupPanel();

        centerPanel.add(soloPanel, "solo");
        centerPanel.add(groupPanel, "group");

        panel.add(centerPanel, BorderLayout.CENTER);

        // Radio button listeners — switch between panels
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        soloButton.addActionListener(e -> cl.show(centerPanel, "solo"));
        groupButton.addActionListener(e -> cl.show(centerPanel, "group"));

        // --- BOTTOM: Save Trip button ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 255, 245));
        JButton saveTripBtn = new JButton("💾 Save Trip");
        styleButton(saveTripBtn, new Color(34, 139, 34));
        bottomPanel.add(saveTripBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Save Trip button action
        saveTripBtn.addActionListener(e -> saveTrip());

        return panel;
    }

    // Builds the Solo Trip input panel
    private JPanel buildSoloPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(245, 255, 245));
        p.setBorder(BorderFactory.createTitledBorder("Solo Trip Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Total Budget (₹):", "Trip Days:", "Places to Visit:", "Expected Daily Expense (₹):"};
        budgetField = new JTextField(18);
        tripDaysField = new JTextField(18);
        placesField = new JTextField(18);
        dailyExpField = new JTextField(18);
        JTextField[] fields = {budgetField, tripDaysField, placesField, dailyExpField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            p.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            p.add(fields[i], gbc);
        }
        return p;
    }

    // Builds the Group Trip input panel
    private JPanel buildGroupPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(new Color(245, 255, 245));
        outer.setBorder(BorderFactory.createTitledBorder("Group Trip Details"));

        JPanel topP = new JPanel(new GridBagLayout());
        topP.setBackground(new Color(245, 255, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Number of people
        gbc.gridx = 0; gbc.gridy = 0;
        topP.add(new JLabel("Number of People:"), gbc);
        numPeopleField = new JTextField(10);
        gbc.gridx = 1;
        topP.add(numPeopleField, gbc);

        // Total budget
        gbc.gridx = 0; gbc.gridy = 1;
        topP.add(new JLabel("Total Budget (₹):"), gbc);
        groupBudgetField = new JTextField(10);
        gbc.gridx = 1;
        topP.add(groupBudgetField, gbc);

        // Button to generate member name/budget fields
        JButton genBtn = new JButton("Generate Member Fields");
        styleButton(genBtn, new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        topP.add(genBtn, gbc);

        outer.add(topP, BorderLayout.NORTH);

        // Scrollable area for member fields
        memberInputPanel = new JPanel();
        memberInputPanel.setLayout(new BoxLayout(memberInputPanel, BoxLayout.Y_AXIS));
        memberInputPanel.setBackground(new Color(245, 255, 245));
        JScrollPane scroll = new JScrollPane(memberInputPanel);
        scroll.setPreferredSize(new Dimension(500, 150));
        outer.add(scroll, BorderLayout.CENTER);

        // Generate member fields when button clicked
        genBtn.addActionListener(e -> {
            String nStr = numPeopleField.getText().trim();
            try {
                numPeople = Integer.parseInt(nStr);
                if (numPeople < 2 || numPeople > 20) {
                    JOptionPane.showMessageDialog(this, "Enter 2 to 20 people.");
                    return;
                }
                memberInputPanel.removeAll();
                memberNameFields = new JTextField[numPeople];
                memberBudgetFields = new JTextField[numPeople];

                for (int i = 0; i < numPeople; i++) {
                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    row.setBackground(new Color(245, 255, 245));
                    row.add(new JLabel("Member " + (i + 1) + " Name:"));
                    memberNameFields[i] = new JTextField(12);
                    row.add(memberNameFields[i]);
                    row.add(new JLabel("Budget (₹):"));
                    memberBudgetFields[i] = new JTextField(8);
                    row.add(memberBudgetFields[i]);
                    memberInputPanel.add(row);
                }
                memberInputPanel.revalidate();
                memberInputPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!");
            }
        });

        return outer;
    }

    // Called when "Save Trip" is clicked
    private void saveTrip() {
        if (userProfile == null) {
            JOptionPane.showMessageDialog(this, "Please save your profile first!");
            return;
        }
        String tName = tripNameField.getText().trim();
        if (tName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a trip name!");
            return;
        }

        if (soloButton.isSelected()) {
            // --- Save Solo Trip ---
            try {
                double budget = Double.parseDouble(budgetField.getText().trim());
                int days = Integer.parseInt(tripDaysField.getText().trim());
                String places = placesField.getText().trim();
                double dailyExp = Double.parseDouble(dailyExpField.getText().trim());

                currentTrip = new Trip(tName, budget, days, places, dailyExp);
                userProfile.addTrip();
                tripCountLabel.setText(String.valueOf(userProfile.getTripCount()));

                JOptionPane.showMessageDialog(this, "Solo trip '" + tName + "' saved!");
                updateTripDetailsTab();
                tabbedPane.setSelectedIndex(2); // Go to Trip Details tab

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please fill all Solo fields correctly!");
            }

        } else {
            // --- Save Group Trip ---
            try {
                int n = numPeople;
                double budget = Double.parseDouble(groupBudgetField.getText().trim());

                if (memberNameFields == null || n == 0) {
                    JOptionPane.showMessageDialog(this, "Please generate member fields first!");
                    return;
                }

                String[] names = new String[n];
                double[] budgets = new double[n];

                for (int i = 0; i < n; i++) {
                    names[i] = memberNameFields[i].getText().trim();
                    budgets[i] = Double.parseDouble(memberBudgetFields[i].getText().trim());
                    if (names[i].isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Enter name for Member " + (i + 1));
                        return;
                    }
                }

                currentTrip = new Trip(tName, n, budget, names, budgets);
                userProfile.addTrip();
                tripCountLabel.setText(String.valueOf(userProfile.getTripCount()));

                JOptionPane.showMessageDialog(this, "Group trip '" + tName + "' saved!");
                updateTripDetailsTab();
                tabbedPane.setSelectedIndex(2); // Go to Trip Details tab

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please fill all Group fields correctly!");
            }
        }
    }

    // ============================================================
    // TAB 3: TRIP DETAILS TAB
    // ============================================================
    private JPanel createTripDetailsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 250, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel title = new JLabel("Trip Details");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(200, 80, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        // Labels (left side)
        String[] fieldLabels = {"Profile Name:", "Trip Name:", "Trip Type:", "Total Budget:", "Total Spent:", "Remaining:"};
        detailProfile = new JLabel("--");
        detailTripName = new JLabel("--");
        detailTripType = new JLabel("--");
        detailBudget = new JLabel("--");
        detailSpent = new JLabel("--");
        detailRemaining = new JLabel("--");
        JLabel[] valueLabels = {detailProfile, detailTripName, detailTripType, detailBudget, detailSpent, detailRemaining};

        for (int i = 0; i < fieldLabels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            panel.add(new JLabel(fieldLabels[i]), gbc);
            valueLabels[i].setFont(new Font("Arial", Font.BOLD, 13));
            valueLabels[i].setForeground(new Color(50, 50, 150));
            gbc.gridx = 1;
            panel.add(valueLabels[i], gbc);
        }

        // Members area (for group trips)
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Members:"), gbc);
        detailMembersArea = new JTextArea(4, 25);
        detailMembersArea.setEditable(false);
        detailMembersArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        detailMembersArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        gbc.gridx = 1;
        panel.add(new JScrollPane(detailMembersArea), gbc);

        // Refresh button
        JButton refreshBtn = new JButton("🔄 Refresh Details");
        styleButton(refreshBtn, new Color(200, 80, 0));
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(refreshBtn, gbc);
        refreshBtn.addActionListener(e -> updateTripDetailsTab());

        return panel;
    }

    // Updates the Trip Details tab with latest data
    private void updateTripDetailsTab() {
        if (userProfile != null) {
            detailProfile.setText(userProfile.getName() + " (Age: " + userProfile.getAge() + ")");
        }
        if (currentTrip != null) {
            detailTripName.setText(currentTrip.getTripName());
            detailTripType.setText(currentTrip.getTripType());
            detailBudget.setText("₹ " + currentTrip.getTotalBudget());
            detailSpent.setText("₹ " + currentTrip.getTotalSpent());

            double rem = currentTrip.getRemainingAmount();
            detailRemaining.setText("₹ " + rem);
            detailRemaining.setForeground(rem >= 0 ? new Color(0, 128, 0) : Color.RED);

            // Show members for group trip
            if (currentTrip.tripType.equals("Group") && currentTrip.memberNames != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < currentTrip.memberNames.length; i++) {
                    sb.append(currentTrip.memberNames[i])
                      .append("  →  ₹ ")
                      .append(currentTrip.memberBudgets[i])
                      .append("\n");
                }
                detailMembersArea.setText(sb.toString());
            } else {
                detailMembersArea.setText("Solo Trip — No members");
            }
        }
    }

    // ============================================================
    // TAB 4: EXPENSES TAB
    // ============================================================
    private JPanel createExpensesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 245, 255));

        // --- TOP: Input fields for a new expense ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 245, 255));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Place
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Place Visited:"), gbc);
        expPlaceField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(expPlaceField, gbc);

        // Amount
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(new JLabel("Amount (₹):"), gbc);
        expAmountField = new JTextField(10);
        gbc.gridx = 3;
        inputPanel.add(expAmountField, gbc);

        // Person
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Spent By:"), gbc);
        expPersonField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(expPersonField, gbc);

        // Date
        gbc.gridx = 2; gbc.gridy = 1;
        inputPanel.add(new JLabel("Date / Day:"), gbc);
        expDateField = new JTextField(10);
        gbc.gridx = 3;
        inputPanel.add(expDateField, gbc);

        // Add button
        JButton addExpBtn = new JButton("➕ Add Expense");
        styleButton(addExpBtn, new Color(148, 0, 211));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        inputPanel.add(addExpBtn, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        // --- CENTER: Table to show all expenses ---
        String[] columns = {"#", "Place", "Amount (₹)", "Spent By", "Date/Day"};
        expenseTableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(expenseTableModel);
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Add Expense button action
        addExpBtn.addActionListener(e -> {
            if (currentTrip == null) {
                JOptionPane.showMessageDialog(this, "Please create a trip first!");
                return;
            }
            String place = expPlaceField.getText().trim();
            String amtStr = expAmountField.getText().trim();
            String person = expPersonField.getText().trim();
            String date = expDateField.getText().trim();

            if (place.isEmpty() || amtStr.isEmpty() || person.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all expense fields!");
                return;
            }
            try {
                double amt = Double.parseDouble(amtStr);

                // Save expense in array
                if (expenseCount < expenses.length) {
                    expenses[expenseCount] = new Expense(place, amt, person, date);
                    expenseCount++;

                    // Update trip's total spent
                    currentTrip.addExpense(amt);

                    // Add row to table
                    expenseTableModel.addRow(new Object[]{expenseCount, place, amt, person, date});

                    // Clear input fields
                    expPlaceField.setText("");
                    expAmountField.setText("");
                    expPersonField.setText("");
                    expDateField.setText("");

                    JOptionPane.showMessageDialog(this, "Expense added!");
                } else {
                    JOptionPane.showMessageDialog(this, "Expense limit reached (100).");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a number!");
            }
        });

        return panel;
    }

    // ============================================================
    // TAB 5: SPLIT CALCULATOR TAB
    // ============================================================
    private JPanel createSplitTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 255, 240));

        // --- TOP: Input fields ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 255, 240));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Split Calculator"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Total amount
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Total Amount (₹):"), gbc);
        splitTotalField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(splitTotalField, gbc);

        // Number of people
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Number of People:"), gbc);
        splitPeopleField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(splitPeopleField, gbc);

        // Calculate button
        JButton calcBtn = new JButton("🧮 Calculate Split");
        styleButton(calcBtn, new Color(0, 150, 50));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(calcBtn, gbc);

        // Use Trip Total button
        JButton useTripBtn = new JButton("Use Current Trip Total");
        useTripBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 3;
        inputPanel.add(useTripBtn, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        // --- CENTER: Result display ---
        splitResultArea = new JTextArea();
        splitResultArea.setEditable(false);
        splitResultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        splitResultArea.setBackground(new Color(240, 255, 240));
        splitResultArea.setBorder(BorderFactory.createTitledBorder("Split Result"));
        panel.add(new JScrollPane(splitResultArea), BorderLayout.CENTER);

        // --- Calculate button action ---
        calcBtn.addActionListener(e -> {
            try {
                double total = Double.parseDouble(splitTotalField.getText().trim());
                int people = Integer.parseInt(splitPeopleField.getText().trim());

                if (people <= 0) {
                    JOptionPane.showMessageDialog(this, "Number of people must be at least 1!");
                    return;
                }

                double equalShare = total / people;
                double extraPerPerson = total - (equalShare * people); // rounding leftover

                StringBuilder result = new StringBuilder();
                result.append("============================\n");
                result.append("   SPLIT CALCULATION RESULT\n");
                result.append("============================\n");
                result.append(String.format("Total Amount    : ₹ %.2f%n", total));
                result.append(String.format("Number of People: %d%n", people));
                result.append("----------------------------\n");
                result.append(String.format("Equal Share Each: ₹ %.2f%n", equalShare));

                // Show per-person breakdown
                result.append("\nBreakdown:\n");
                for (int i = 1; i <= people; i++) {
                    result.append(String.format("  Person %d  →  ₹ %.2f%n", i, equalShare));
                }

                result.append("============================\n");

                // If group trip exists, compare each member's budget to their share
                if (currentTrip != null && currentTrip.tripType.equals("Group")
                        && currentTrip.memberNames != null) {
                    result.append("\nMember Budget vs. Equal Share:\n");
                    for (int i = 0; i < currentTrip.memberNames.length; i++) {
                        double diff = currentTrip.memberBudgets[i] - equalShare;
                        String status = diff >= 0 ? "✅ Extra: ₹" + String.format("%.2f", diff)
                                                   : "❌ Short: ₹" + String.format("%.2f", Math.abs(diff));
                        result.append(String.format("  %-15s → %s%n", currentTrip.memberNames[i], status));
                    }
                }

                splitResultArea.setText(result.toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
            }
        });

        // Use current trip total button
        useTripBtn.addActionListener(e -> {
            if (currentTrip == null) {
                JOptionPane.showMessageDialog(this, "No trip found! Create a trip first.");
                return;
            }
            splitTotalField.setText(String.valueOf(currentTrip.getTotalSpent()));
            if (currentTrip.tripType.equals("Group")) {
                splitPeopleField.setText(String.valueOf(currentTrip.numberOfPeople));
            }
        });

        return panel;
    }

    // ============================================================
    // HELPER: Style a button with a custom color
    // ============================================================
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    // ============================================================
    // MAIN METHOD: Entry point of the application
    // ============================================================
    public static void main(String[] args) {
        // Use the system look-and-feel for a nicer appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If it fails, default Swing look-and-feel is used
        }

        // Launch the app on the Event Dispatch Thread (Swing best practice)
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
