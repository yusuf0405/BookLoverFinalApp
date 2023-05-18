package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info

import android.os.Bundle
import android.view.View
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.pager_adapter.UserInfoPagerAdapter
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.FragmentStudentDetailsBinding
import com.example.data.cache.models.IdResourceString
import com.example.domain.models.StudentDomain
import com.google.android.material.tabs.TabLayoutMediator
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentUserInfo :
    BaseFragment<FragmentStudentDetailsBinding, FragmentUserInfoViewModel>(
        FragmentStudentDetailsBinding::inflate
    ) {

    private val userId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentUserInfoArgs.fromBundle(requireArguments()).userId
    }


    @Inject
    lateinit var factory: FragmentUserInfoViewModel.Factory
    override val viewModel: FragmentUserInfoViewModel by assistedViewModel {
        factory.create(userId = userId)
    }

    private val defaultConfigureStrategy = TabLayoutMediator.TabConfigurationStrategy { _, _ -> }
    private var tabConfigureStrategy = defaultConfigureStrategy

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding().includeUserInfoBlock) {
        pager.adapter = UserInfoPagerAdapter(
            userId = userId,
            fragmentManager = childFragmentManager,
            lifecycle = viewLifecycleOwner.lifecycle
        )
        tabConfigureStrategy = TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = context?.getText(
                when (position) {
                    0 -> R.string.progress
                    else -> R.string.books
                }
            )
        }
        tabLayoutMediator = TabLayoutMediator(tabLayout, pager, tabConfigureStrategy)
        tabLayoutMediator?.attach()
    }


    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            userFlow.observe(::handleUserFetching)
            isErrorFlow.observe(::showErrorSnackBar)
        }
    }

    private fun handleUserFetching(user: StudentDomain) = with(binding()) {
        val fullName = "${user.name} ${user.lastname}"
        userFullNameText.text = fullName
//        telephoneNumber.text = user.number
        includeUserInfoToolbarBlock.userToolbarNameText.text = fullName
        requireContext().showImage(user.image.url, userImage)
    }

    private fun showErrorSnackBar(errorId: IdResourceString) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(errorId.format(requireContext()))
            .build()
            .show()

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        tabConfigureStrategy = defaultConfigureStrategy
        binding().includeUserInfoBlock.pager.adapter = null
        super.onDestroyView()
    }
}