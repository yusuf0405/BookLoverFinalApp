buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    val build_gradle_version = "8.1.4"
    val kotlin_version = "1.9.20"
    val google_service_version = "4.3.15"
    val hilt_version = "2.48.1"
    val navigation_version = "2.7.5"

    dependencies {
        classpath("com.android.tools.build:gradle:$build_gradle_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.gms:google-services:$google_service_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
    }
}

