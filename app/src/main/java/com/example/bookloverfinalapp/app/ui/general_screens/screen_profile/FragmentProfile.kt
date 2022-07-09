package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.app.utils.setting.SettingManager.clearAppSettings
import com.example.bookloverfinalapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

internal object List {
    @JvmStatic
    fun main(args: ArrayList<String>) {

    }

}

@AndroidEntryPoint
class FragmentProfile :
    BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(
        FragmentProfileBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInClickListeners()
        setupUi()
    }

    private fun setInClickListeners() {
        binding().apply {
            downEffect(editProfileBtn).setOnClickListener(this@FragmentProfile)
            downEffect(profileLogoutImg).setOnClickListener(this@FragmentProfile)
            downEffect(profileLogoutText).setOnClickListener(this@FragmentProfile)
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
        lifecycleScope.launch {
            loadingDialog.show()
            viewModel.clearDataInCache()
            SharedPreferences().saveIsFilter(false, requireContext())
            clearAppSettings(scope = lifecycleScope, requireContext())
            loadingDialog.dismiss()
            requireActivity().intentClearTask(activity = ActivityLoginMain())
        }
    }

    private fun setupUi() {
        val student = SharedPreferences().getCurrentUser(activity = requireActivity())
        binding().apply {
            setCardViewColor(materialCardView)
            val fullName = "${student.name} ${student.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = student.schoolName
            profileClassText.text = student.className
            requireActivity().glide(student.image?.url, binding().profileImg)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}