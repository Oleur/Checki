# Checki

### Rquirements
* Android Studio 3.3 or higher
* Kotlin 1.3.11
* Kotlin Coroutines 1.1.0
* Lifecycle 2.0.0
* Room 2.1.0-alpha03
* AndroidX libraries and ConstraintLayout 2.0.0-alpha3

### Deployment
Run the project with Android Studio to deploy it on your device or the emulator

### Features
In this application you can see a list of services and their status (Up or Down), and the time we last checked. Add a new service to check or remove one already checked. Thanks to Room database all services are stored between app launches.
A simple but clear design based on the Material Design guidelines.

### Improvements
* Improve the list management with the DiffUtils library. This will notify the items that really changed not all of them.
* Polish the UI for a better user experience.
* Add more empty states and error messages to notify the user if something failed
* Undo the deletion of a service in the list
