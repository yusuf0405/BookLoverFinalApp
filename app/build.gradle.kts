plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.bookloverfinalapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.bookloverfinalapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = false
}
dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core:ui-core"))
    implementation(project(":core:utils-core"))
    implementation(project(":core:theme"))
    implementation(project(":core:common"))
    implementation(project(":features:profile"))
    implementation(project(":features:select-favorite-book"))
    implementation(project(":features:stories"))
    implementation(project(":features:sign-in"))
    implementation(project(":features:onboarding"))

    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)
    implementation(libs.viewpager2)
    implementation(libs.material)

    implementation(libs.camera)
    implementation(libs.camera.view)
    implementation(libs.camera.core)
    implementation(libs.camera.core.lifecycle)

    implementation(libs.preference)
    implementation(libs.datastore.preferences)

    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.process)
    implementation(libs.lifecycle.service)

    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragments)

    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    implementation(libs.itextg)
    implementation(libs.palette)

    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.mediasession)

    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    
    implementation(libs.work.runtime.ktx)
    implementation(libs.work.gcm)

    implementation(libs.hilt.work.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.work.compiler)

    implementation(libs.firebase.crashlytics)
    implementation(libs.statistics.library)
    implementation(libs.parse.android)
    implementation(libs.pushdown.anim.click)
    implementation(libs.glide)
    implementation(libs.lingver)
    implementation(libs.circular.progress.view)
    implementation(libs.pdf.viewer)
    implementation(libs.loading.animation)
    implementation(libs.mask.edittext)
    implementation(libs.circleimageview)
    implementation(libs.prettytime)
    implementation(libs.facebook.shimmer)
    implementation(libs.lottie)
    implementation(libs.materialedittext.library)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.io.mockk)

    androidTestImplementation(libs.work.testing)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.mockito.android)
}