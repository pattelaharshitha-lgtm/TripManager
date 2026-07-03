// ============================================================
// Profile.java
// This class stores the user's personal profile information.
// It keeps track of name, age, and how many trips were created.
// ============================================================

public class Profile {

    // --- Fields (data stored in the profile) ---
    String name;       // User's full name
    int age;           // User's age
    int tripCount;     // Number of trips created so far

    // --- Constructor: called when we create a new Profile object ---
    public Profile(String name, int age) {
        this.name = name;
        this.age = age;
        this.tripCount = 0; // Start with 0 trips
    }

    // --- Increment trip count by 1 each time a trip is created ---
    public void addTrip() {
        tripCount++;
    }

    // --- Getters: methods to read the values ---
    public String getName() { return name; }
    public int getAge() { return age; }
    public int getTripCount() { return tripCount; }
}
