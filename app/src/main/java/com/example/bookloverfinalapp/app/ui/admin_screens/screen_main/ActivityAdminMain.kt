package com.example.bookloverfinalapp.app.ui.admin_screens.screen_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.setting.SettingManager
import com.example.bookloverfinalapp.databinding.ActivityAdminMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityAdminMain : AppCompatActivity() {
    val binding: ActivityAdminMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAdminMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SettingManager.setAppSetting(scope = lifecycleScope, context = this)
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_main_admin))
    }
}