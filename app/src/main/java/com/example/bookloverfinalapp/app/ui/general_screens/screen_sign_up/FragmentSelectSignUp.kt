package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.admin_sign_up.FragmentAdminSignUpDialog
import com.joseph.utils_core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentSelectSignUpBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.utils_core.extensions.showOnlyOne

class FragmentSelectSignUp : Fragment() {

    private var _binding: FragmentSelectSignUpBinding? = null
    val binding get()  = _binding ?: bindingLifecycleError()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


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