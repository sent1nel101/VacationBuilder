# D308 Mobile Application - Vacation & Excursion Planner

## Purpose of the Application

This Android application serves as a mobile scheduling tool for managing vacations and their associated excursions. Users can:
*   View a list of all their planned vacations.
*   Add new vacations, including details like title, hotel name, start date, and end date.
*   Edit existing vacation details.
*   Delete vacations (only if they have no associated excursions).
*   For each vacation, users can view, add, edit, and delete associated excursions, including excursion name and date.
*   Set reminder notifications for the start and end dates of vacations and for excursion start dates.
*   Share vacation details (including associated excursions) via other apps.

The application utilizes a local SQLite database via Room Persistence Library to store and manage all vacation and excursion data.

## Directions for How to Operate the Application & Meet Rubric Aspects

This application is designed to be intuitive. Follow these steps to explore its features:
**0. Home Screen** 
*   Press the **"Enter"** button to enter the application.
**1. Main Screen (Vacation List):**
* **Adding Sample Data (if needed)**
*   From the main screen's menu, there is an option called "Add Sample Data." This populates the app with pre-defined vacations and excursions for quick testing.
*   **View Vacations:** Upon launching, you'll see a list of all saved vacations if they are present. Otherwise the list will be empty and you have to add sample data, mentioned above, or create a new vacation.
*   **Add New Vacation:**
*   Tap the **Floating Action Button (FAB)** with a `+` icon (usually at the bottom right).
*   This navigates to the "Vacation Details" screen for a new vacation.
*   **Edit Existing Vacation:**
*   Tap on any vacation item in the list.
*   This navigates to the "Vacation Details" screen, pre-filled with that vacation's information.

**2. Vacation Details Screen:**
*   **Fields:**
*   **Title:** Enter/edit the name of the vacation (e.g., "Paris Getaway").
*   **Hotel Name:** Enter/edit the name of the hotel.
*   **Start Date:** Tap to open a date picker to select/edit the vacation's start date.
*   **End Date:** Tap to open a date picker to select/edit the vacation's end date.
*   **Saving a Vacation:**
*   After filling in the details, tap the **"Save Vacation"** option in the Action Bar.
*   *New Vacations:* A new vacation record is created.
*   *Existing Vacations:* The existing record is updated.
*   You will be returned to the main vacation list.
*   **Deleting a Vacation:**
*   Tap the **"Delete Vacation"** option in the menu.
*   A vacation **cannot** be deleted if it has associated excursions. A toast message will indicate this. If no excursions are associated, the vacation will be deleted, and you'll be returned to the vacation list.
*   **Setting Vacation Alerts:**
*   Tap the **"Set Start Date Alert"** option in the menu (e.g., a bell icon or specific text). This will schedule an alarm (Toast message) to notify you on the morning of the vacation's start date.  If the date is in the past, message will fire immediately.
*   Tap the **"Set End Date Alert"** option in the menu. This will schedule an alarm (Toast message) to notify you on the morning of the vacation's end date.  If the date is in the past, message will fire immediately.
*   Alerts can only be set if the vacation is saved (has an ID) and the relevant dates are filled. Toast messages will confirm scheduling.
*   **Note about setting alerts for start/end dates:**
*   A Toast message will appear saying the permission for exact reminder is not set, but the notification will still work.
*   **Sharing Vacation Details:**
*   Tap the **"Share"** option in the menu (usually a share icon).
*   This will compile the vacation title, hotel, dates, and a list of all its associated excursions into a text format and open the Android share dialog to send it to another app (e.g., email, messaging). This can only be done for saved vacations with some details filled.
*   **Managing Excursions for this Vacation:**
*   **View Associated Excursions:** A RecyclerView on this screen lists all excursions specifically linked to the current vacation.
*   **Add New Excursion to this Vacation:**
*   Tap the **Floating Action Button (FAB)** with a `+` icon (usually at the bottom right of the Vacation Details screen).
*   This is only possible if the vacation has been saved first.
*   This navigates to the "Excursion Details" screen for a new excursion, pre-linking it to the current vacation and passing the vacation's start/end dates for validation.
*   **Edit Existing Excursion:**
*   Tap on any excursion item in the list on the Vacation Details screen.
*   This navigates to the "Excursion Details" screen, pre-filled with that excursion's information.

**3. Excursion Details Screen:**
*   **Fields:**
*   **Excursion Name:** Enter/edit the name of the excursion (e.g., "Eiffel Tower Visit").
*   **Excursion Date:** Tap to open a date picker to select/edit the excursion's date.
*   **Saving an Excursion:**
*   After filling in the details, tap the **"Save Excursion"** option in the menu.
*   The excursion date **must** fall within the start and end dates of its parent vacation. All fields are required. Date validation is performed.
*   *New Excursions:* A new excursion record is created and linked to the parent vacation.
*   *Existing Excursions:* The existing record is updated.
*   You will be returned to the "Vacation Details" screen.
*   **Deleting an Excursion:**
*   Tap the **"Delete Excursion"** option in the menu. The excursion will be deleted.
*   You will be returned to the "Vacation Details" screen.
*   **Setting Excursion Alerts:**
*   Tap the **"Notify"** option in the menu. This will schedule an alarm (Toast message) to notify you on the morning of the excursion's date. If the date is in the past, message will fire immediately.
*   *Rubric:* Alerts can only be set if the excursion is saved and the date is filled. Toast messages will confirm scheduling.

**4. Navigating Back:**
*   Use the standard Android back arrow (top left of the app bar or system back button) to navigate to the previous screen (e.g., from Excursion Details back to Vacation Details, or from Vacation Details back to the Vacation List).

**Rubric Aspects Summary:**
*   **CRUD Operations:** Fully implemented for both Vacations and Excursions.
*   **Data Validation:** Implemented for dates (vacation end vs. start, excursion date within vacation period) and required fields.
*   **Alerts/Notifications:** Implemented for vacation start/end dates and excursion dates using `AlarmManager` and `BroadcastReceiver`.
*   **Sharing:** Implemented for sharing vacation details (including excursions) using an `Intent.ACTION_SEND`.
*   **UI:** RecyclerViews for displaying lists, DatePickers for date input, standard Android UI components.
*   **Database:** Room Persistence Library is used for all data storage. Operations that could block the UI (database access) are performed on background threads.

## Target Android Version for Signed APK

The signed APK is configured to target:
*   **`minSdk`**: 26 (Android 8.0 Oreo)
*   **`targetSdk`**: 35 (Android 15 VanillaIceCream)
*   **`compileSdk`**: 35 (Android 15 VanillaIceCream)
