package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main.ActivityAdminMain
import com.example.bookloverfinalapp.app.ui.general_screens.screen_class_has_deleted.ActivityClassHasDeleted
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_STUDENT_EDITOR_SAVE_KEY
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_STUDENT_SAVE_KEY
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.app.utils.setting.SettingManager
import com.example.bookloverfinalapp.databinding.ActivitySplashBinding
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetClassUseCase
import com.example.domain.interactor.GetCurrentUserUseCase
import com.example.domain.models.UserDomain
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActivitySplash : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private var currentUser: User? = null

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @Inject
    lateinit var getClassUseCase: GetClassUseCase

    @Inject
    lateinit var mapper: Mapper<UserDomain, User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SettingManager.setAppSetting(scope = lifecycleScope, context = this)
        val pref = getSharedPreferences(CURRENT_STUDENT_EDITOR_SAVE_KEY, Context.MODE_PRIVATE)
        currentUser =
            Gson().fromJson(pref.getString(CURRENT_STUDENT_SAVE_KEY, null), User::class.java)

        lifecycleScope.launch {
            if (currentUser == null) {
                delay(3000)
                intentClearTask(activity = ActivityLoginMain())
            } else {
                if (currentUser!!.userType == UserType.admin) {
                    delay(3000)
                    intentClearTask(activity = ActivityAdminMain())

                } else checkUserStatus()
            }
        }
    }

    private suspend fun checkUserStatus() {
        if (NavigationManager().isOnline(this@ActivitySplash)) {
            getCurrentUserUseCase.execute(currentUser!!.sessionToken).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> binding.progressBar.showView()

                    Status.SUCCESS -> {
                        SharedPreferences().saveCurrentUser(mapper.map(resource.data!!), this@ActivitySplash)
                        checkUserClass()
                    }
                    Status.EMPTY -> {
                        showToast(message = getString(R.string.you_account_deleted))
                        NavigationManager().loginOut(this@ActivitySplash)
                        intentClearTask(activity = ActivityLoginMain())
                    }
                    Status.ERROR -> {
                        binding.progressBar.hideView()
                        createErrorDialog(message = resource.message!!)
                    }
                }
            }
        } else {
            delay(3000)
            intentClearTask(activity = ActivityMain())
        }
    }

    private fun checkUserClass() = lifecycleScope.launch {
        getClassUseCase.execute(id = currentUser!!.classId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> binding.progressBar.showView()
                Status.SUCCESS -> intentClearTask(activity = ActivityMain())
                Status.EMPTY -> intentClearTask(activity = ActivityClassHasDeleted())
                Status.ERROR -> {
                    binding.progressBar.hideView()
                    createErrorDialog(message = resource.message!!)
                }
            }
        }
    }

    private fun createErrorDialog(message: String) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> lifecycleScope.launch { checkUserStatus() }
            }
        }
        this.shoErrorDialog(message = message, listener = listener)
    }
}