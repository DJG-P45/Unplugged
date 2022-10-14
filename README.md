# Unplugged Documentation
Unplugged is an Android mobile application that displays South Africa loadshedding schedules for areas selected by the user. The load shedding schedule for a particular area on a particular day is presented in a vertical 24 hour day timeline format (See 2nd screenshot). The total load shedding (downtime) for a particular day is calculated and displayed alongside the schedule. A user can add and remove areas for which they wish to receive loadshedding schedules. The loadshedding schedules are provided by the [EskomSePush](https://sepush.co.za) backend API.

This mobile application aims to provide essential loadshedding information with a user experience that does not overwhelm the user with options and information. In other words, it keeps things minimal and simple.

## Screenshots
<img src="https://github.com/Dale-Gathercole-P45/Unplugged/blob/1f3d43634ab4daa19dfb592cf012b8ed03da6753/screenshots/Dashboard_Screen.png" width="200"/> |
<img src="https://github.com/Dale-Gathercole-P45/Unplugged/blob/1f3d43634ab4daa19dfb592cf012b8ed03da6753/screenshots/Schedule_Screen.png" width="200"/> |
<img src="https://github.com/Dale-Gathercole-P45/Unplugged/blob/1f3d43634ab4daa19dfb592cf012b8ed03da6753/screenshots/FindAreas_Screen.png" width="200"/>

## Built With
- Java - The primary programming language.
- RxJava - For handling asynchronous operations.
- Volley - For making requests to the EskomSePush API.
- Room - For persistence operations.
- Mockito - For testing.
- Android Studio Chipmunk IDE

## Solution Setup
Follow these steps to set up the Unplugged Android project locally.
You will require latest Android Studio to build and run an Android application.

1. Clone the Unplugged Android project to your local machine.
2. Import the local project into your Android Studio IDE.
3. Create the file named "apikey.properties" in the project root folder.
4. In the newly created properties file add ```ESKOM_SE_PUSH_KEY="<key-here>"```
5. Where it says "<key-here>" provide your own EskomSePush API key.
6. Build and run the project.

## App Usage
Below are some brief details explaining how to use the app.

### Screens
The app has three screens which are detailed below.

- **Home screen:** The first screen the user is presented with after launching the application. Here the user can see select areas for which they wish to see loadshedding schedules.
- **Schedule Screen:** This screen displays the loadshedding schedule for a particular area on a particular day in a vertical 24 hour day timeline format (See 2nd screenshot). A user can switch between dates.
- **Find Areas Screen:** This screen is where a user can search for a specific area to add to the Home screen.

### Instructions
- **Adding an area:** 
  1. Tap the plus button when viewing the home screen.
  2. Type an approximate area name in the search input.
  3. Tap the search icon (magnifying glass).
  4. Select an area from the search results to add it.
  5. Repeat steps 2 to 4 if none of the search result are correct.
- **Removing an area:**
  1. swipe left on the area when viewing the home screen.
- **Viewing a loadshedding schedule:** 
  1. Tap an area when viewing the home screen.