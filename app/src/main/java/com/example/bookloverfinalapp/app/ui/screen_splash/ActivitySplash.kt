package com.example.bookloverfinalapp.app.ui.screen_splash

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main.ActivityAdminMain
import com.example.bookloverfinalapp.app.ui.screen_class_has_deleted.ActivityClassHasDeleted
import com.example.bookloverfinalapp.app.ui.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_EDITOR_STUDENT_SAVE_KEY
import com.example.bookloverfinalapp.app.utils.cons.CURRENT_STUDENT_SAVE_KEY
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
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
        val pref = getSharedPreferences(CURRENT_EDITOR_STUDENT_SAVE_KEY, Context.MODE_PRIVATE)
        currentUser = Gson().fromJson(pref.getString(CURRENT_STUDENT_SAVE_KEY, null), User::class.java)

        lifecycleScope.launch {
            if (currentUser == null) {
                delay(3000)
                intentClearTask(activity = ActivityLoginMain())
            } else {
                if (currentUser!!.userType == UserType.admin) {
                    delay(3000)
                    intentClearTask(activity = ActivityAdminMain())

                } else chekNavigation()
            }
        }
    }

    private suspend fun chekNavigation() {
        if (CheсkNavigation().isOnline(this@ActivitySplash)) {
            getCurrentUserUseCase.execute(currentUser!!.sessionToken).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> binding.progressBar.showView()

                    Status.SUCCESS -> {
                        CurrentUser().saveCurrentUser(mapper.map(resource.data!!),
                            this@ActivitySplash)
                        chekClass()
                    }
                    Status.EMPTY -> {
                        showToast(message = getString(R.string.you_account_deleted))
                        CheсkNavigation().loginOut(this@ActivitySplash)
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

    private fun chekClass() = lifecycleScope.launch {
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
                DialogInterface.BUTTON_POSITIVE -> lifecycleScope.launch { chekNavigation() }
            }
        }
        this.shoErrorDialog(message = message, listener = listener)
    }
}