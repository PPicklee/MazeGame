
# Maze Master

Maze Master is a top-down maze game built with Kotlin for Android devices. Players navigate procedurally generated mazes, avoid obstacles, and find the exit.





## Prerequisites
To run the project, you need the following software and tools installed:

Android Studio
Download from Android Studio.
JDK (Java Development Kit) 17 or later
Ensure Java is set up and the environment variables are configured.
Kotlin Plugin
Kotlin is built into Android Studio; no additional installation is needed.
Android SDK
Available within Android Studio. Ensure it includes API Level 21 or higher.
## Libraries/Dependencies
The project uses the following libraries:

Core SplashScreen API

Dependency: androidx.core:core-splashscreen:1.0.0
Jetpack Compose (Optional)
If you're using Jetpack Compose for UI, ensure you have the required Compose dependencies in the build.gradle file.
## How to Set Up and Run the Project

Clone the Repository

Clone the project repository to your local machine using:
bash
Copy code
git clone <repository-url>
Replace <repository-url> with the actual URL of your repository.
Open in Android Studio

Launch Android Studio and open the project folder.
Sync Gradle Files

Android Studio will prompt you to sync the Gradle files. Click on "Sync Now".
Install Dependencies

Ensure all dependencies in build.gradle are installed by syncing the project.
Set Up an Emulator or Device

Set up an Android emulator (API Level 21 or higher).
Alternatively, connect a physical device via USB with Developer Mode enabled.
Build and Run the Project

Click on the green "Run" button in Android Studio or press Shift + F10.
Select the target device or emulator, and the app will launch.
## Gameplay Instructions
Objective
Navigate through the maze, avoid obstacles, and find the exit to win.

Controls

Use on-screen buttons or touch gestures to move the character.
Tap the Start button to begin the game.
Scoring

The faster you complete the maze, the higher your score.
Game Over

If you hit an obstacle or run out of time, the game ends.
## Troubleshooting
Gradle Sync Issues
Ensure you have a stable internet connection and valid proxy settings.

Emulator Not Starting
Install the correct emulator image in the Android Virtual Device (AVD) Manager.
## Known Bugs
Timer Malfunction

The game timer may stop prematurely under certain conditions, particularly when pausing or resuming the game. This issue causes the game logic to behave inconsistently.
Score Mistracking

The score calculation occasionally fails to account for time spent or maze complexity, leading to inaccurate final scores. This is especially noticeable in larger mazes or when the timer malfunction occurs.
Workaround
For now, restarting the game from the main menu may resolve temporary inconsistencies. However, these bugs will be addressed in a future update.
## Authors

- [@Julio Ortega](https://github.com/PPicklee)
Feel free to reach out at ortega57gabriel@gmail.com for queries or feedback.

