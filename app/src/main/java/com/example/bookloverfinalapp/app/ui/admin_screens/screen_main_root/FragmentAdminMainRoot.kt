package com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.utils.extensions.setTabLayoutColor
import com.example.bookloverfinalapp.databinding.FragmentAdminMainRootBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentAdminMainRoot : Fragment() {

    private val binding: FragmentAdminMainRootBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAdminMainRootBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTabLayoutColor(tabLayout = binding.tabLayout)
        setupTabLayout()

    }

    private fun setupTabLayout() {
//        binding.apply {
//            viewPager.isSaveEnabled = false
//            viewPager.adapter =
//                AdminViewPagerAdapter(fm = requireActivity().supportFragmentManager,
//                    lifecycle = lifecycle)
//            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
//                when (pos) {
//                    0 -> tab.text = getString(R.string.admin_books)
//                    1 -> tab.text = getString(R.string.admin_classes)
//                }
//            }.attach()
//        }
    }

}
