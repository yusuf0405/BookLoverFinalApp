plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle.plugin)

    implementation(kotlin("script-runtime"))
}