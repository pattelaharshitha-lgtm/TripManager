// ============================================================
// Trip.java
// This class stores all information about a single trip.
// It handles both Solo Trips and Group Trips.
// ============================================================

public class Trip {

    // --- Basic trip info ---
    String tripName;       // Name of the trip (e.g., "Goa Trip")
    String tripType;       // "Solo" or "Group"

    // --- Solo trip fields ---
    double totalBudget;    // Total money available
    int tripDays;          // How many days the trip lasts
    String placesToVisit;  // Places planned to visit
    double expectedDailyExpense; // Expected spending per day

    // --- Group trip fields ---
    int numberOfPeople;          // How many people in the group
    String[] memberNames;        // Array to store each member's name
    double[] memberBudgets;      // Array to store each member's budget share

    // --- Expense tracking (used across tabs) ---
    double totalSpent;     // Total money spent so far

    // -------------------------------------------------------
    // Constructor for SOLO TRIP
    // -------------------------------------------------------
    public Trip(String tripName, double totalBudget, int tripDays,
                String placesToVisit, double expectedDailyExpense) {
        this.tripName = tripName;
        this.tripType = "Solo";
        this.totalBudget = totalBudget;
        this.tripDays = tripDays;
        this.placesToVisit = placesToVisit;
        this.expectedDailyExpense = expectedDailyExpense;
        this.totalSpent = 0;
    }

    // -------------------------------------------------------
    // Constructor for GROUP TRIP
    // -------------------------------------------------------
    public Trip(String tripName, int numberOfPeople,
                double totalBudget, String[] memberNames,
                double[] memberBudgets) {
        this.tripName = tripName;
        this.tripType = "Group";
        this.numberOfPeople = numberOfPeople;
        this.totalBudget = totalBudget;
        this.memberNames = memberNames;
        this.memberBudgets = memberBudgets;
        this.totalSpent = 0;
    }

    // --- Add an expense amount to total spent ---
    public void addExpense(double amount) {
        totalSpent += amount;
    }

    // --- Calculate how much money is remaining ---
    public double getRemainingAmount() {
        return totalBudget - totalSpent;
    }

    // --- Getters ---
    public String getTripName() { return tripName; }
    public String getTripType() { return tripType; }
    public double getTotalBudget() { return totalBudget; }
    public double getTotalSpent() { return totalSpent; }
}
