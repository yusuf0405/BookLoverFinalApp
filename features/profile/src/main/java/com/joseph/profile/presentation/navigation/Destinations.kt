package com.joseph.profile.presentation.navigation

interface Destinations {
    val title: String
    val route: String
    val routeWithArgs: String
}

object Profile : Destinations {

    override val title: String
        get() = "Profile"

    override val route: String
        get() = "profile"

    override val routeWithArgs: String
        get() = route
}

object EditProfile : Destinations {

    override val title: String
        get() = "Edit Profile"

    override val route: String
        get() = "edit_profile"

    override val routeWithArgs: String
        get() = route
}

val profileDestinations = listOf(Profile, EditProfile)