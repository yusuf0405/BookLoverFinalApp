package com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_my_books.FragmentMyBooks
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.FragmentAllBooks

class StudentViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAllBooks()
            else -> FragmentMyBooks()
        }
    }
}
