package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_camera.FragmentChooseFromCamera
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_gallery.FragmentChoosePosterGallery
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online.FragmentChoosePosterOnline


class ChooseQuestionPosterPagerAdapter(
    private val onNetworkPosterSelectedListener: (String) -> Unit,
    private val onLocalPosterSelectedListener: (String) -> Unit,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    companion object {
        private const val TABS_COUNT = 3
    }

    override fun getItemCount(): Int = TABS_COUNT

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FragmentChoosePosterOnline.newInstance(onNetworkPosterSelectedListener)
            1 -> FragmentChoosePosterGallery.newInstance(onLocalPosterSelectedListener)
            2 -> FragmentChooseFromCamera()
            else -> throw IndexOutOfBoundsException("Exiting the number of tabs")
        }
}