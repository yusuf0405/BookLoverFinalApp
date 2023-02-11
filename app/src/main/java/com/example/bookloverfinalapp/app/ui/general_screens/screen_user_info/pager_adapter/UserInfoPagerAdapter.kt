package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_progress.FragmentUserProgress
import com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_read_books.FragmentUserReadBooks


class UserInfoPagerAdapter(
    private val userId: String,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    companion object {
        private const val TABS_COUNT = 2
    }

    override fun getItemCount(): Int = TABS_COUNT

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FragmentUserProgress.newInstance(userId = userId)
            1 -> FragmentUserReadBooks.newInstance(userId = userId)
            else -> throw IndexOutOfBoundsException("Exiting the number of tabs")
        }
}