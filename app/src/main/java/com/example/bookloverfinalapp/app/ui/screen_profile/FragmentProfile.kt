package com.example.bookloverfinalapp.app.ui.screen_profile

import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.utils.SettingManager
import com.example.bookloverfinalapp.app.utils.extensions.glide
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentProfile :
    BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(
        FragmentProfileBinding::inflate), View.OnClickListener {
    override val viewModel: FragmentProfileViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInClickListeners()
        setupUi()
    }

    private fun setInClickListeners() {
        binding().apply {
            editProfileBtn.setOnClickListener(this@FragmentProfile)
            profileLogoutImg.setOnClickListener(this@FragmentProfile)
            profileLogoutText.setOnClickListener(this@FragmentProfile)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().profileLogoutImg -> confirmation()
            binding().profileLogoutText -> confirmation()
            binding().editProfileBtn -> viewModel.goEditProfileFragment()
        }
    }

    private fun confirmation() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> loginOut()
                DialogInterface.BUTTON_NEGATIVE -> {}
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.login_out_title)
            .setCancelable(true)
            .setMessage(R.string.default_login_out_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()

        dialog.show()
    }

    private fun loginOut() {
        viewModel.clearDataInCache()
        CheсkNavigation().apply {
            observeLogin(status = false, activity = requireActivity())
            loginOut(activity = requireActivity())
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requireActivity().intentClearTask(ActivityLoginMain())
    }

    private fun setupUi() {
        val student = CurrentUser().getCurrentUser(activity = requireActivity())
        binding().apply {
            when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> materialCardView.setBackgroundColor(Color.parseColor("#2A00A2"))
                Configuration.UI_MODE_NIGHT_YES -> materialCardView.setBackgroundColor(Color.parseColor("#305F72"))
            }
            val fullName = "${student.name} ${student.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = student.schoolName
            profileClassText.text = student.className
            requireActivity().glide(student.image?.url, binding().profileImg)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}