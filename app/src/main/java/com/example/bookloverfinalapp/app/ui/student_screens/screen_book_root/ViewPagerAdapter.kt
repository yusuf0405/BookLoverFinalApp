package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.ui.FragmentStudentBooks
import com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.ui.FragmentStudentAllBooks

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentStudentAllBooks()
            else -> FragmentStudentBooks()
        }
    }
}
