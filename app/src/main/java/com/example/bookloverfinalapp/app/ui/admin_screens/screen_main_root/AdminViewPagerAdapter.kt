package com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.ui.FragmentBooks
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ui.FragmentClasses

class AdminViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentBooks()
            else -> FragmentClasses()
        }
    }
}
