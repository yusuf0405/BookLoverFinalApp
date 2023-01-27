# A program written entirely in Kotlin using the principles of Clean Architecture according to the MVVM pattern.

PDF reader app for schools, where readers can read pdf e-books and  
answer questions about each chapter’s content. Each reader gets scored according  
to their answers, reading speed, number of completed book, etc. Includes admin  
functions to manage all readers’ data, statistics for all users, add and manage  
books

## Screens

[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/856e42f13ff98d69071cbb1242495715_screenshot_main_screen.jpg" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/856e42f13ff98d69071cbb1242495715_screenshot_main_screen.jpg)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/e8b9601a55b5f6a6d67eb03de45994ee_screenshot_profile_screen.jpg" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/e8b9601a55b5f6a6d67eb03de45994ee_screenshot_profile_screen.jpg)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/e53436753456dab0c80300a4696c18f1_screenshot_player.jpg" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/e53436753456dab0c80300a4696c18f1_screenshot_player.jpg)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/c53ccb080d2a9c6debfee553477d5c3b_screenshot_statistics_screen.jpg" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/c53ccb080d2a9c6debfee553477d5c3b_screenshot_statistics_screen.jpg)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/abe84ed38c5c8122e2665a7334e90e6b_screenshot_leadershboard_screen.jpg" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/abe84ed38c5c8122e2665a7334e90e6b_screenshot_leadershboard_screen.jpg)

## Animations
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/406b97f876acd542a3abfda16f087090_app_first_animate.gif" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/406b97f876acd542a3abfda16f087090_app_first_animate.gif)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/688bbbb9a9b8d9bd567257d0f7628df1_app_second_animate.gif" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/688bbbb9a9b8d9bd567257d0f7628df1_app_second_animate.gif)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/6c2b708b262b4fba1a38103a8b3a577a_app_third_animate.gif" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/6c2b708b262b4fba1a38103a8b3a577a_app_third_animate.gif)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/4070ef1bb977ddb8c0de9e5da2b7e0bc_app_fouth_animate.gif" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/4070ef1bb977ddb8c0de9e5da2b7e0bc_app_fouth_animate.gif)
[<img src="https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/14586408ae76e5412aaeff0cfcac3912_app_fifth_animate.gif" width=160>](https://parsefiles.back4app.com/wjeyjDXpdkgLCnrAPffqinj1YKoZu5ZSnKx4NmFo/14586408ae76e5412aaeff0cfcac3912_app_fifth_animate.gif)

## Libraries

### Android Jetpack

* [ViewBinding](https://developer.android.com/topic/libraries/view-binding) View binding is a
  feature that makes it easier for you to write code that interacts with views.

* [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) An interface
  that automatically responds to lifecycle events.

* [Preference](https://developer.android.com/jetpack/androidx/releases/preference) Build interactive
  settings screens without needing to interact with device storage or manage the UI.

* [Navigation](https://developer.android.com/guide/navigation?gclsrc=aw.ds&gclid=Cj0KCQiA09eQBhCxARIsAAYRiymyM6hTEs0cGr5ZCXOWtLhVUwDK1O86vf8V_Uq2DWvVYNFZwPFznzAaAllMEALw_wcB)
  Navigation refers to interactions that allow users to navigate through , enter, and exit various
  parts of the content in your app. Navigation component Android Jetpack helps you navigate, from
  simple button clicks to more complex templates like application panels and navigation bar. The
  navigation component also provides a consistent and predictable user interface, adhering to an
  established set of principles.

* [Room](https://developer.android.com/jetpack/androidx/releases/room) The Room persistence library
  provides an abstraction layer over SQLite to allow for more robust database access while
  harnessing the full power of SQLite.

* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) Data objects that
  notify views of changes to the underlying database.

* [Kotlin flows](https://developer.android.com/kotlin/flow) In coroutines, a flow is a type that can
  emit multiple values sequentially, as opposed to suspend functions that return only a single
  value. For example, you can use a flow to receive live updates from a database.

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) Data related to
  the user interface that is not destroyed when the application is rotated. Easily schedule
  asynchronous tasks for optimal execution.

### DI

* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) Hilt is a
  dependency injection library for Android that reduces the execution time of manual dependency
  injection into your project. Performing manual dependency injection requires that you create each
  class and its dependencies manually, and use containers to reuse and manage dependencies.

### Image

* [Glide](https://github.com/bumptech/glide) Glide is a fast and efficient open source media
  management and image loading framework for Android that wraps media decoding, memory and disk
  caching, and resource pooling into a simple and easy to use interface.

### Network

* [Retrofit2](https://github.com/square/retrofit) Type-safe HTTP client for Android and Java.

* [OkHttp](https://github.com/square/okhttp) HTTP + HTTP/2 client for Android and Java applications.

* [Parse-SDK-Android](https://github.com/parse-community/Parse-SDK-Android) A library that gives you
  access to the powerful Parse Server backend from your Android app. For more information about
  Parse and its features, see the website, getting started, and blog.

### Coroutines

* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) Coroutines is a rich library for
  coroutines developed by JetBrains. It contains a number of high-level primitives with support for
  coroutines, which are discussed in this guide, including startup, asynchrony, and others.

### GitHub Custom Libraries

* [Android Pdf Viewer](https://github.com/barteksc/AndroidPdfViewer) Library for displaying PDF
  documents on Android, with animations, gestures, zoom and double tap support. It is based on
  PdfiumAndroid for decoding PDF files. Works on API 11 (Android 3.0) and higher. Licensed under
  Apache License 2.0.

* [Circle Image View](https://github.com/hdodenhof/CircleImageView) A fast circular ImageView
  perfect for profile images. This is based on RoundedImageView from Vince Mi which itself is based
  on techniques recommended by Romain Guy.

* [Circular Progress View](https://github.com/VaibhavLakhera/Circular-Progress-View) A customisable
  circular progress view for android.

* [Lingver](https://github.com/YarikSOffice/lingver) Lingver is a library to manage your application
  locale and language.

* [Push Down Animation Click](https://github.com/nontravis/pushdown-anim-click) A library for
  Android developers who want to create "push down animation click" for view like spotify
  application. :)

* [Maskara](https://github.com/santalu/maskara) A simple way to format text fields without getting
  affected by input filters.

* [Expandable TextView](https://github.com/Manabu-GT/ExpandableTextView) ExpandableTextView is an
  Android library that allows developers to easily create an TextView which can expand/collapse just
  like the Google Play's app description. Feel free to use it all you want in your Android apps
  provided that you cite this project.

* [Flow Layout](https://github.com/ApmeM/android-flowlayout) Extended linear layout that wrap its
  content when there is no place in the current line.

* [Pretty Time](https://github.com/ocpsoft/prettytime) Social Style Date and Time Formatting for
  Java

* [Shimmer](https://github.com/facebook/shimmer-android) Shimmer is an Android library that provides
  an easy way to add a shimmer effect to any view in your Android app.



