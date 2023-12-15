plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.joseph.onboarding"

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
    implementation(project(":core:common"))
    implementation(project(":core:ui-core"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
}