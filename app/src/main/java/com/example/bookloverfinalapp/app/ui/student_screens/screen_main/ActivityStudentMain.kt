package com.example.bookloverfinalapp.app.ui.student_screens.screen_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.databinding.ActivityStudentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityStudentMain : AppCompatActivity() {

    val binding: ActivityStudentMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityStudentMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_main_student))
    }
}