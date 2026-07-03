=====================================================
  TRIP MONEY MANAGEMENT SYSTEM - Setup Instructions
=====================================================

FILES IN THIS PROJECT (all .java files):
  1. Profile.java      - Stores user profile data
  2. Trip.java         - Stores trip details (Solo/Group)
  3. Expense.java      - Stores individual expense entries
  4. MainFrame.java    - Main GUI window with all 5 tabs

HOW TO RUN IN ECLIPSE:
  1. Open Eclipse → File → New → Java Project
  2. Name it "TripMoneyManagement"
  3. Right-click src folder → Import → File System
  4. Select all 4 .java files
  5. Right-click MainFrame.java → Run As → Java Application

HOW TO RUN IN NetBeans:
  1. File → New Project → Java Application
  2. Uncheck "Create Main Class"
  3. Right-click Source Packages → Add Existing Files
  4. Select all 4 .java files
  5. Right-click MainFrame.java → Run File

HOW TO RUN FROM COMMAND LINE:
  1. Navigate to the folder with the .java files
  2. Compile: javac *.java
  3. Run:     java MainFrame

=====================================================
  HOW TO USE THE APP
=====================================================

Step 1 → Profile Tab
  - Enter your name and age
  - Click "Save Profile"
  - Then click "Create Trip"

Step 2 → Create Trip Tab
  - Enter a trip name
  - Select Solo or Group trip
  - Fill in the details
  - Click "Save Trip"

Step 3 → Trip Details Tab
  - See all your trip info at a glance
  - Budget, spent, and remaining amounts

Step 4 → Expenses Tab
  - Add each expense as you spend
  - All expenses appear in a table

Step 5 → Split Calculator Tab
  - Enter total and number of people
  - Or click "Use Current Trip Total"
  - See the equal split breakdown

=====================================================
