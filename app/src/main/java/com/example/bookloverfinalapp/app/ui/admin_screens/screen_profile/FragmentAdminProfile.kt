package com.example.bookloverfinalapp.app.ui.admin_screens.screen_profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.screen_login_main.ActivityLoginMain
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentAdminProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAdminProfile :
    BaseFragment<FragmentAdminProfileBinding, FragmentAdminProfileViewModel>(
        FragmentAdminProfileBinding::inflate),View.OnClickListener {

    override val viewModel: FragmentAdminProfileViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setInClickListeners()

    }
    private fun setInClickListeners() {
        binding().apply {
            editProfileBtn.setOnClickListener(this@FragmentAdminProfile)
            profileLogoutImg.setOnClickListener(this@FragmentAdminProfile)
            profileLogoutText.setOnClickListener(this@FragmentAdminProfile)
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
        requireActivity().intentClearTask(ActivityLoginMain())
    }

    private fun setupUi() {
        val student = CurrentUser().getCurrentUser(activity = requireActivity())
        binding().apply {
            val fullName = "${student.name} ${student.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = student.schoolName

            Glide.with(requireActivity())
                .load(student.image?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(binding().profileImg)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }


}