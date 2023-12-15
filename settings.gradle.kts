pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jcenter.bintray.com")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.google.com")
    }
}
rootProject.name = "KnigolyubApp"

include(
    ":app",
    ":domain",
    ":data",
    ":core:ui-core",
    ":core:utils-core",
    ":core:theme",
    ":core:common",
    ":features:select-favorite-book",
    ":features:sign-in",
    ":features:profile",
    ":features:onboarding",
    ":features:stories"
)
