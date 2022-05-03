package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.ActivityTeacherMain
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.MainActivity
import com.example.bookloverfinalapp.app.ui.student_screens.screen_main.ActivityStudentMain
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
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
            when {
                CheсkNavigation().readLoginStatus(activity = this@ActivitySplash) -> {
                    intentClearTask(activity = ActivityStudentMain())
                }
                else -> intentClearTask(activity = MainActivity())
            }
        }
    }
}