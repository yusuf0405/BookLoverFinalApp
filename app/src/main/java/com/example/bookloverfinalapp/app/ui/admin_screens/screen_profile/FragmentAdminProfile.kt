package com.example.bookloverfinalapp.app.ui.admin_screens.screen_profile

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentAdminProfileBinding
import com.joseph.core.extensions.showImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentAdminProfile :
    BaseFragment<FragmentAdminProfileBinding, FragmentAdminProfileViewModel>(
        FragmentAdminProfileBinding::inflate
    ), View.OnClickListener {

    override val viewModel: FragmentAdminProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setupUi()
        setInClickListeners()

    }

    private fun setInClickListeners() {
        binding().apply {
//            downEffect(editProfileBtn).setOnClickListener(this@FragmentAdminProfile)
            downEffect(profileLogoutImg).setOnClickListener(this@FragmentAdminProfile)
            downEffect(profileLogoutText).setOnClickListener(this@FragmentAdminProfile)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().profileLogoutImg -> confirmation()
            binding().profileLogoutText -> confirmation()
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
            viewModel.clearDataInCache()
            SharedPreferences().saveCurrentUser(User.unknown(), requireActivity())
        }
    }

    private fun setupUi() {
        val admin =
            SharedPreferences().getCurrentUser(activity = requireActivity()) ?: User.unknown()
        binding().apply {
            setCardViewColor(materialCardView)
            val fullName = "${admin.name} ${admin.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = admin.schoolName
            requireContext().showImage(admin.image?.url, profileImg)
        }
    }
}