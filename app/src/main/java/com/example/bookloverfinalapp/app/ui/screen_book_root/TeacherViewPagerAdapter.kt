package com.example.bookloverfinalapp.app.ui.screen_book_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.screen_my_students.ui.FragmentMyStudents
import com.example.bookloverfinalapp.app.ui.screen_all_books.ui.FragmentAllBooks
import com.example.bookloverfinalapp.app.ui.screen_my_books.ui.FragmentMyBooks

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