package com.example.bookloverfinalapp.app.ui.general_screens.activity_main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.player.BasePlayerActivity
import com.example.bookloverfinalapp.app.utils.extensions.findNavControllerById
import com.example.bookloverfinalapp.app.utils.setting.SettingManager.setAppSetting
import com.example.bookloverfinalapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityMain : BasePlayerActivity() {

    override val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setAppSetting(scope = lifecycleScope, context = this)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        initBottomNavigationView(navController)
    }

    private fun initBottomNavigationView(navController: NavController) {
        binding.bottomNavigationView.setOnItemReselectedListener {
            val currentLocation = navController.currentDestination?.id
            val currentLabel = navController.currentDestination?.label
            when (it.itemId) {
                R.id.main_screen_nav_graph -> {
                    if (currentLocation != R.id.fragmentMainScreen) {
                        navController.popBackStack(R.id.fragmentMainScreen, false)
                        currentLocation?.apply(navController::navigate)
                    } else {
                        findViewById<RecyclerView>(R.id.book_recycler_view)
                            ?.smoothScrollToPosition(0)
                    }

                }
                R.id.progress_nav_graph -> {
//                    if (currentLocation != R.id.fragment_progress) {
//                        navController.popBackStack(R.id.fragment_progress, false)
//                    } else {
////                        findViewById<LinearLayout>(R.id.root_linear_layout_mine)?.scrollFirstScrollableViewGroupIfCan()
//                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment =
            findChildFragmentManagerById(R.id.nav_host_fragment_activity_main)
                .fragments[0]

        if (currentFragment is OnBackPressedListener) {
            if (currentFragment.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }

    private fun findChildFragmentManagerById(fragmentContainerId: Int): FragmentManager {
        val contentFragment = supportFragmentManager.findFragmentById(fragmentContainerId)
            ?: throw IllegalStateException("ChildFragmentManager is not initialized, because no found fragment")
        return contentFragment.childFragmentManager
    }

}