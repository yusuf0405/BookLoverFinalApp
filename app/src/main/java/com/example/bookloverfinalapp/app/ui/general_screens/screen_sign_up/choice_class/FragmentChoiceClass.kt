package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_class

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.ui.general_screens.ProgressDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentChoiceClassBinding
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchOnLifecycle
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.showOnlyOne
import com.joseph.utils_core.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentChoiceClass :
    BaseFragment<FragmentChoiceClassBinding, FragmentChoiceClassViewModel>(
        FragmentChoiceClassBinding::inflate
    ), SettingSelectionAdapter.OnItemSelectionListener {

    @Inject
    lateinit var factory: FragmentChoiceClassViewModel.Factory
    override val viewModel: FragmentChoiceClassViewModel by viewModelCreator {
        factory.create(schoolId = getSchoolId(), userSignUp = getUserSignUp())
    }

    private fun getSchoolId() = requireArguments().getString(SCHOOL_ID_KEY) ?: String()

    private fun getUserSignUp() = requireArguments().getParcelable(USER_KEY) ?: UserSignUp()

    private val adapter: SettingSelectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionAdapter(onItemSelectionListener = this)
    }

    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setOnClickListeners()
        observeData()
    }

    private fun setOnClickListeners() = with(binding()) {
        nextButton.setOnDownEffectClickListener { viewModel.startSignUp() }
    }

    private fun setupRecyclerView() = with(binding()) {
        classRecyclerView.adapter = adapter
        classRecyclerView.itemAnimator = null
    }

    private fun observeData() = with(viewModel) {
        launchOnLifecycle {
            isProgressBarVisibleFlow.observe(::setProgressBarVisibility)
        }
        launchWhenViewStarted {
            settingSelectionFlow.observe(adapter::populate)
            isErrorMessageVisibleFlow.observe(::setErrorMessageVisibility)
            isProgressDialogVisibleFlow.observe(::handleProgressDialogStatus)
            handleSignUpFlow.observe { handleSignUp() }
        }
    }

    private fun handleSignUp() {
        dismissModalPage()
        navControllerPopBackStackInclusive()
        findNavController().navigate(
            R.id.main_navigation,
            bundleOf(),
            createNavOptionsWithAnimations()
        )
    }

    private fun createNavOptionsWithAnimations() = NavOptions
        .Builder()
        .setEnterAnim(R.anim.slide_up)
        .setExitAnim(R.anim.slide_down)
        .setPopEnterAnim(R.anim.slide_up)
        .setPopExitAnim(R.anim.slide_down)
        .build()

    private fun navControllerPopBackStackInclusive() =
        findNavController().popBackStack(R.id.login_navigation, false)

    override fun onItemSelected(item: SettingSelectionItem) {
        viewModel.handleItemOnClickListener(item.id)
    }

    private fun handleProgressDialogStatus(isShow: Boolean) {
        if (isShow) progressDialog.showOnlyOne(parentFragmentManager)
        else progressDialog.dismiss()
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        binding().progressBar.isVisible = isVisible
        binding().nextButton.isVisible = !isVisible
    }

    private fun setErrorMessageVisibility(isVisible: Boolean) {
        binding().errorMessage.isVisible = isVisible
    }

    companion object {
        private const val USER_KEY = "USER_KEY"
        private const val SCHOOL_ID_KEY = "SCHOOL_ID_KEY"

        fun newInstance(user: UserSignUp, schoolId: String) =
            FragmentChoiceClass().apply {
                arguments = bundleOf(
                    USER_KEY to user,
                    SCHOOL_ID_KEY to schoolId,
                )
            }
    }
}