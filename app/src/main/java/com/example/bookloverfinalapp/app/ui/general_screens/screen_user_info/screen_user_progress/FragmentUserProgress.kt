package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_progress

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.databinding.FragmentUserProgressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUserProgress :
    BaseFragment<FragmentUserProgressBinding, FragmentUserProgressViewModel>(
        FragmentUserProgressBinding::inflate
    ) {

    override val viewModel: FragmentUserProgressViewModel by viewModels()


    companion object {
        private const val USER_ID_KEY = "USER_ID_KEY"
        fun newInstance(userId: String) = FragmentUserProgress().apply {
            arguments = bundleOf(USER_ID_KEY to userId)
        }
    }
}