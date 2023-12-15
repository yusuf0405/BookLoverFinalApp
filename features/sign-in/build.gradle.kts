plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.joseph.sign_in"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            val proguards = fileTree("$rootDir/proguard") {
                include("*.pro")
            }
            proguardFiles(*proguards.toList().toTypedArray())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get().toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:ui-core"))
    implementation(project(":core:utils-core"))
    implementation(project(":core:theme"))
    implementation(project(":core:common"))

    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragments)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
}