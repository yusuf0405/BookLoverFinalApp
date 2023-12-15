package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_school

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.joseph.ui.core.R
import com.joseph.ui.core.R as UiCore
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.ui.general_screens.ProgressDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_class.FragmentChoiceClass
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.modalPageNavigateTo
import com.example.bookloverfinalapp.databinding.FragmentChoiceSchoolBinding
import com.example.data.cache.models.IdResourceString
import com.google.android.material.snackbar.Snackbar
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import com.joseph.ui.core.extensions.launchOnLifecycle
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.showOnlyOne
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import com.example.bookloverfinalapp.R as MainRes

@AndroidEntryPoint
class FragmentChoiceSchool :
    BaseFragment<FragmentChoiceSchoolBinding, FragmentChoiceSchoolViewModel>(
        FragmentChoiceSchoolBinding::inflate
    ), SettingSelectionAdapter.OnItemSelectionListener {

    override val viewModel: FragmentChoiceSchoolViewModel by viewModels()

    private val user: UserSignUp by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getParcelable(USER_KEY) ?: UserSignUp()
    }

    private val adapter: SettingSelectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionAdapter(onItemSelectionListener = this)
    }

    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog.getInstance()
    }

    private var isNavigateToChoiceClassFragment = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setOnClickListeners()
        observeData()
    }

    private fun setOnClickListeners() = with(binding()) {
        nextButton.setOnDownEffectClickListener { viewModel.navigateToChoiceClassFragment(user) }
    }

    private fun setupRecyclerView() = with(binding()) {
        schoolRecyclerView.adapter = adapter
        schoolRecyclerView.itemAnimator = null
    }

    private fun observeData() = with(viewModel) {
        launchOnLifecycle {
            isProgressBarVisibleFlow.observe(::setProgressBarVisibility)
        }
        launchWhenViewStarted {
            settingSelectionFlow.filterNotNull().observe(adapter::populate)
            isErrorMessageVisibleFlow.observe(::setErrorMessageVisibility)
            handleButtonOnClickFlow.observe(::handleButtonOnClick)
            isShowErrorNotificationFlow.observe(::showErrorSnackbar)
            isProgressDialogVisibleFlow.observe(::handleProgressDialogStatus)
            handleSignUpFlow.observe { handleSignUp() }
        }
    }

    private fun handleButtonOnClick(schoolId: String) {
        if (isNavigateToChoiceClassFragment) navigateToSelectionFragment(schoolId = schoolId)
        else viewModel.startUserSignUp(user)
    }

    private fun handleProgressDialogStatus(isShow: Boolean) {
        if (isShow) progressDialog.showOnlyOne(parentFragmentManager)
        else progressDialog.dismiss()
    }

    private fun handleSignUp() {
        dismissModalPage()
        navControllerPopBackStackInclusive()
        findNavController().navigate(
            MainRes.id.admin_navigation,
            bundleOf(),
            createNavOptionsWithAnimations()
        )
    }

    private fun createNavOptionsWithAnimations() = NavOptions
        .Builder()
        .setEnterAnim(UiCore.anim.slide_up)
        .setExitAnim(UiCore.anim.slide_down)
        .setPopEnterAnim(UiCore.anim.slide_up)
        .setPopExitAnim(UiCore.anim.slide_down)
        .build()

    private fun navControllerPopBackStackInclusive() =
        findNavController().popBackStack(MainRes.id.login_navigation, false)

    private fun showErrorSnackbar(messageId: IdResourceString) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .duration(Snackbar.LENGTH_SHORT.toLong())
            .message(messageId.format(requireContext()))
            .build()
            .show()

    private fun navigateToSelectionFragment(schoolId: String) {
        val newFragment = FragmentChoiceClass.newInstance(
            user = user,
            schoolId = schoolId,
        )
        modalPageNavigateTo(
            newFragment = newFragment,
            title = getString(UiCore.string.class_setup),
            rootContainer = binding().rootContainer,
            transitionName = binding().rootContainer.transitionName
        )
        binding().root.translationX = -resources.displayMetrics.widthPixels.toFloat()
    }

    private fun setProgressBarVisibility(isVisible: Boolean) {
        binding().progressBar.isVisible = isVisible
        binding().nextButton.isVisible = !isVisible
    }

    private fun setErrorMessageVisibility(isVisible: Boolean) {
        binding().errorMessage.isVisible = isVisible
    }

    override fun onItemSelected(item: SettingSelectionItem) {
        viewModel.setItemOnClickListener(item.id)
    }

    companion object {
        private const val USER_KEY = "USER_KEY"
        fun newInstance(
            userSignUp: UserSignUp,
            isNavigateToChoiceClassFragment: Boolean = true
        ) = FragmentChoiceSchool().apply {
            this.isNavigateToChoiceClassFragment = isNavigateToChoiceClassFragment
            arguments = bundleOf(USER_KEY to userSignUp)
        }
    }

}