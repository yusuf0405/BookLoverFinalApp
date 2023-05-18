package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_read_books

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookFingerprint
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.databinding.FragmentUserReadBooksBinding
import com.example.data.cache.models.IdResourceString
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentUserReadBooks :
    BaseFragment<FragmentUserReadBooksBinding, FragmentUserReadBooksViewModel>(
        FragmentUserReadBooksBinding::inflate
    ) {

    private val userId: String by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getString(USER_ID_KEY) ?: String()
    }

    @Inject
    lateinit var factory: FragmentUserReadBooksViewModel.Factory
    override val viewModel: FragmentUserReadBooksViewModel by assistedViewModel {
        factory.create(userId = userId)
    }

    private val adapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            SavedBookFingerprint()
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        recyclerView.adapter = adapter
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            userBooksFlow.observe(adapter::submitList)
            isErrorFlow.observe(::showErrorSnackBar)
        }
    }

    private fun showErrorSnackBar(errorId: IdResourceString) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(errorId.format(requireContext()))
            .build()
            .show()


    companion object {
        private const val USER_ID_KEY = "USER_ID_KEY"
        fun newInstance(userId: String) = FragmentUserReadBooks().apply {
            arguments = bundleOf(USER_ID_KEY to userId)
        }
    }
}