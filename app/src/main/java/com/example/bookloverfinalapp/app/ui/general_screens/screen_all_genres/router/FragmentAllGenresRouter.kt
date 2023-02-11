package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.router

import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentAllGenresRouter {

    fun navigateRoGenreInfoFragment(genreCode: String): NavCommand
}