package com.example.bookloverfinalapp.app.ui.screen_splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main.ActivityAdminMain
import com.example.bookloverfinalapp.app.ui.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivitySplash : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {

            delay(3000)
            intentClearTask(activity = ActivityAdminMain())

//            when {
//                CheсkNavigation().readLoginStatus(activity = this@ActivitySplash) -> {
//                    intentClearTask(activity = ActivityMain())
//                }
//                else -> intentClearTask(activity = ActivityLoginMain())
//            }
        }
    }
}