package com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_my_students.FragmentMyStudents
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.FragmentAllBooks
import com.example.bookloverfinalapp.app.ui.general_screens.screen_my_books.FragmentMyBooks

class TeacherViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAllBooks()
            1 -> FragmentMyBooks()
            else -> FragmentMyStudents()
        }
    }
}