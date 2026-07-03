// ============================================================
// Expense.java
// This class stores a single expense entry.
// Each time someone spends money, we create one Expense object.
// ============================================================

public class Expense {

    // --- Fields for each expense ---
    String place;      // Where the money was spent (e.g., "Beach Resort")
    double amount;     // How much was spent
    String spentBy;    // Who spent the money
    String date;       // Date or day number (e.g., "Day 1" or "12-Jun-2025")

    // --- Constructor: fill in all details when creating an expense ---
    public Expense(String place, double amount, String spentBy, String date) {
        this.place = place;
        this.amount = amount;
        this.spentBy = spentBy;
        this.date = date;
    }

    // --- Getters: methods to read each field ---
    public String getPlace() { return place; }
    public double getAmount() { return amount; }
    public String getSpentBy() { return spentBy; }
    public String getDate() { return date; }
}
