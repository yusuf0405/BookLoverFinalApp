package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentBookRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class FragmentRootStudentBook : Fragment() {

    private val binding: FragmentStudentBookRootBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentBookRootBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        binding.apply {
            viewPager.isSaveEnabled = false
            viewPager.adapter = ViewPagerAdapter(fm = requireActivity().supportFragmentManager,
                lifecycle = lifecycle)
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.all_books)
                    1 -> tab.text = getString(R.string.my_books)
                }
            }.attach()
        }
    }
}