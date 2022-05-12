package com.example.bookloverfinalapp.app.ui.screen_main_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.screen_my_books.ui.FragmentMyBooks
import com.example.bookloverfinalapp.app.ui.screen_all_books.ui.FragmentAllBooks

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
