# Atos - mSafe

# 🏁 Final Product feature set (17-06)
| feature set                                        | State 
|---                                                 |---    
Be able to search files                              | ✔️    |            
Mark files as favorites                              | ✔️    |
Add files to the normal and hidden section           | ✔️    |
Download files from the normal section               | ✔️    |
Download files from the hidden section               | ✔️    |
Share files from the hidden and normal section       | ✔️    |  
Delete files from the normal and hidden section      | ✔️    |
Click on the file to open the full file              | ✔️    |          
Change name in the app                               | ✔️    |
A code to enter the hidden section of the app        | ✔️    |
Move files from normal to hidden section(vice versa) | ✔️    |
Secret entrance for the hidden files                 | ✔️    |      
Share files from other apps to mSafe                 | ✔️    |      

# 🛠 Built With

- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android
  development.
- [Glide](https://github.com/bumptech/glide) - Glide is the best image loading library for Android.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - A coroutine is a
  concurrency design pattern that you can use on Android to simplify code that executes
  asynchronously.
- [Firebase](https://firebase.google.com/) - Firebase is an app development platform that helps you build and grow apps users love. Backed by Google and trusted by millions of businesses.
- [Firestore]( https://firebase.google.com/products/firestore) - Flexible, scalable NoSQL cloud database to store and sync data for client- and server-side development.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) -
  Collection of libraries that help you design robust, testable, and maintainable apps.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores
      UI-related data that isn"t destroyed on UI changes.
- [Material Components for Android](https://github.com/material-components/material-components-android)
    - Modular and customizable Material Design UI components for Android.
- [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer) - The flexible, easy to use, all in one drawer library for your Android project.


## 📦 File structure
```
📦atos
 ┗ 📂msafe
 ┃ ┣ 📂core                         # All code for the "core" of the app
 ┃ ┃ ┣ 📂adapter                    # adapter for the recylerview
 ┃ ┃ ┃ ┗ 📜FileAdapter.kt
 ┃ ┃ ┣ 📂ui                         # All fragments within the "core"
 ┃ ┃ ┃ ┣ 📜AccountFragment.kt
 ┃ ┃ ┃ ┣ 📜HomeFragment.kt
 ┃ ┃ ┃ ┗ 📜SavedFragment.kt
 ┃ ┃ ┣ 📂viewmodel                  # Viewmodel to go with the fragments of the "Core"
 ┃ ┃ ┃ ┣ 📜AccountViewModel.kt
 ┃ ┃ ┃ ┗ 📜CoreViewModel.kt
 ┃ ┃ ┣ 📜CoreActivity.kt            # Acitivity for the "Core"
 ┃ ┃ ┗ 📜ManagePermission.kt        # Class to handle the permissions needed for the app
 ┃ ┣ 📂model                        # Data classes
 ┃ ┃ ┣ 📜ItemFile.kt
 ┃ ┃ ┣ 📜NewUser.kt
 ┃ ┃ ┗ 📜User.kt
 ┃ ┣ 📂repository
 ┃ ┃ ┗ 📜FirebaseRepository.kt      # Firebase repository to make API calls to it
 ┃ ┣ 📂setup                        # All the code for the "Setup" of the app
 ┃ ┃ ┣ 📂ui                         # All fragments within the "Setup"
 ┃ ┃ ┃ ┣ 📜BootFragment.kt
 ┃ ┃ ┃ ┣ 📜LoginFragment.kt
 ┃ ┃ ┃ ┣ 📜SetupFragment.kt
 ┃ ┃ ┃ ┗ 📜SignupFragment.kt
 ┃ ┃ ┣ 📂viewmodel                  # Viewmodel to go with the fragments of the "Setup"
 ┃ ┃ ┃ ┣ 📜LoginViewModel.kt
 ┃ ┃ ┃ ┗ 📜SignupViewModel.kt
 ┃ ┃ ┗ 📜SetupActivity.kt           # Acitivity for the "Setup"
 ┃ ┗ 📂shared
 ┃ ┃ ┗ 📜Extensions.kt              # File that contains all the extensions
```   
