package com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.utils.extensions.setTabLayoutColor
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentMainRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class FragmentRootStudentBook : Fragment() {

    private val binding: FragmentMainRootBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentMainRootBinding.inflate(layoutInflater)
    }
    private val currentUser: User by lazy(LazyThreadSafetyMode.NONE) {
        SharedPreferences().getCurrentUser(activity = requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentUser.userType == UserType.student) setupTabLayout()
        else setupTeacherTabLayout()
        setTabLayoutColor(tabLayout = binding.tabLayout)

    }

    private fun setupTabLayout() {
        binding.apply {
            viewPager.isSaveEnabled = false
            viewPager.adapter = StudentViewPagerAdapter(fm = requireActivity().supportFragmentManager, lifecycle = lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.all_books)
                    1 -> tab.text = getString(R.string.my_books)
                }
            }.attach()
        }
    }

    private fun setupTeacherTabLayout() {
        binding.apply {
            viewPager.isSaveEnabled = false
            viewPager.adapter =
                TeacherViewPagerAdapter(fm = requireActivity().supportFragmentManager,
                    lifecycle = lifecycle)
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.all_books)
                    1 -> tab.text = getString(R.string.my_books)
                    2 -> tab.text = getString(R.string.my_students)
                }
            }.attach()
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}