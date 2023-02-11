package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.admin_sign_up.FragmentAdminSignUpDialog
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showOnlyOne
import com.example.bookloverfinalapp.databinding.FragmentSelectSignUpBinding
import com.joseph.ui_core.custom.modal_page.ModalPage

class FragmentSelectSignUp :
    BaseBindingFragment<FragmentSelectSignUpBinding>(FragmentSelectSignUpBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        studentBtn.setOnDownEffectClickListener {
            navigateSignUpFragment(userType = UserType.student)
        }
        teacherBtn.setOnDownEffectClickListener {
            navigateSignUpFragment(userType = UserType.teacher)
        }
        adminBtn.setOnDownEffectClickListener {
            showAdminSignUpDialog()
        }
        alreadyRegisteredBlock.signInLink.setOnDownEffectClickListener {
            navigateLoginFragment()
        }
        settingButton.setOnDownEffectClickListener {
            showSettingModalPage()
        }
    }

    private fun showAdminSignUpDialog() = FragmentAdminSignUpDialog
        .newInstance()
        .showOnlyOne(parentFragmentManager)

    private fun showSettingModalPage() = FragmentSetting.newInstance(getString(R.string.setting))
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun navigateSignUpFragment(userType: UserType) =
        findNavController().navigate(
            FragmentSelectSignUpDirections.actionFragmentSelectSignUpToFragmentSignUp(userType = userType)
        )

    private fun navigateLoginFragment() =
        findNavController().navigate(FragmentSelectSignUpDirections.actionFragmentSelectSignUpToFragmentLogin())
}