package com.example.bookloverfinalapp.app.ui.general_screens.screen_login_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookloverfinalapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityLoginMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)
    }
}