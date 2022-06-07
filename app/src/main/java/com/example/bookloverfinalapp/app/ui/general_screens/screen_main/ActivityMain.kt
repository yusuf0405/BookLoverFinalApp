package com.example.bookloverfinalapp.app.ui.general_screens.screen_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.setting.SettingManager.setAppSetting
import com.example.bookloverfinalapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setAppSetting(scope = lifecycleScope, context = this)
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_main))
    }
}