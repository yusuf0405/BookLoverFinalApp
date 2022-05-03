package com.example.bookloverfinalapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookloverfinalapp.databinding.ActivityTeacherMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityTeacherMain : AppCompatActivity() {
    val binding: ActivityTeacherMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityTeacherMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.navViewTeacher.setupWithNavController(findNavController(R.id.nav_host_fragment_activity_main_teacher))
    }
}