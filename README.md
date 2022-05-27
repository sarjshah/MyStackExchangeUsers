# StackExchange App
The Stack Exchange App provides users with a list of users from different sites. The data shown in the app is retrieved from the Stackoverflow. 

# Screens
Used UI wireframe diagrams to create 2 screens for the app.
The first screen is for displaying the list of users in a recycler view. 
There should be 20 users displayed and should be sorted in alphabetical order. 
There is search functionality to quickly locate users in the list.
When one of the user is clicked, then user should be navigated to the second screen.
The second screen is to show details of the user selected.

# Project Technology
- MVVM
- Kotlin
- Flow (Coroutines)
- Navigation Component
- Dagger Hilt DI
- View Binding
- Constraint Layout

# Architecture Design
The Project follows a Clean Architecture and MVVM with Repository pattern architecture. 

# Test Cases

# Libraries Used
- Dagger Hilt to provide constructor dependency injection to classes in the application
- Navigation Component has been used for navigating between screens
- Safe Args has been used for passing type safe arguments between screens
- Retrofit to provide access to the backend API endpoints
- Coroutines and Flow to run API network requests on background threads
- AndroidX to provide Lifecycle and LiveData functionality to the app
- View binding to bind the inflated layout files to instances running in the application code
- Glide has been used for processing images

# Further Improvements
- Project can be refactored to use Pagination 3 that has been developed with Kotlin coroutines in mind.
- Refactor to cache data in Room DB.
- Refactor and add workmanager to retrieve data from API in the background.
