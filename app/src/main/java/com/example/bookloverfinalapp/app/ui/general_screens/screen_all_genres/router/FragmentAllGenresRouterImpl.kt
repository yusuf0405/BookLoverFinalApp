package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.router

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.FragmentAllGenresDirections
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentAllGenresRouterImpl @Inject constructor() : FragmentAllGenresRouter {

    override fun navigateRoGenreInfoFragment(genreCode: String) =
        FragmentAllGenresDirections
            .actionFragmentAllGenresToBookGenreInfoNavGraph(genreCode)
            .toNavCommand()
}