# Compose Multiplatform PDFGenerator


This is a Kotlin Multiplatform project targeting Android, iOS with illustrations on how to create a pdf on Android/iOS.<br>
 For those with Xcode versions older  than 15 clone from  the mainCopy Branch.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…<br>

libraries
#Decompose<br>
<br>
What is Decompose?¶<br>
Decompose is a Kotlin Multiplatform library for breaking down your code into lifecycle-aware business logic components (aka BLoC), with routing functionality and pluggable UI (Jetpack Compose, Android Views, SwiftUI, JS React, etc.).<br>
https://arkivanov.github.io/Decompose/getting-started/quick-start/<br>
https://github.com/arkivanov/Decompose<br>

#swift-klib-plugin<br>
This gradle plugin provides easy way to include your Swift source files in your Kotlin Multiplatform Mobile shared module and access them in Kotlin via cinterop for iOS targets.<br>
https://github.com/ttypic/swift-klib-plugin<br>

## Screenshots

### Android

<div style="display: flex; flex-direction: column; align-items: center; gap: 20px;">
    <!-- Top row of images -->
    <div style="display: flex; align-items: center; gap: 30px;">
        <img src="art/android1.jpeg" width="250" />
        <img src="art/android2.jpeg" width="250" />
        <img src="art/android3.jpeg" width="250" />
    </div>
    <!-- Image below -->
    <img src="art/android4.jpeg" width="250" />
</div>

### iOS
<div style="display: flex; flex-direction: column; align-items: center; gap: 20px;">
    <!-- Top row of images -->
    <div style="display: flex; align-items: center; gap: 30px;">
        <img src="art/ios1.PNG" width="250" />
        <img src="art/ios2.PNG" width="250" />
    </div>
    <!-- Image below -->
    <img src="art/ios3.png" width="700" />
</div>





