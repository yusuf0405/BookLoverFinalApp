package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.router

import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.FragmentProgressDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentProgressRouterImpl @Inject constructor() : FragmentProgressRouter {

    override fun navigateToFragmentLeaderboardChart(): NavCommand = FragmentProgressDirections
        .actionFragmentProgressToFragmentLeaderboardChart()
        .toNavCommand()
}