package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.joseph.utils_core.bindingLifecycleError
import com.joseph.utils_core.extensions.tuneBottomDialog
import com.joseph.utils_core.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.FragmentUserSortDialogBinding
import com.joseph.ui_core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUsersSortDialog : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentUserSortDialogBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: SortingUsersOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingUsersOptionsMenuActions>? = null

    override fun onStart() {
        super.onStart()
        tuneBottomDialog()
        tuneLyricsDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSortDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(com.joseph.ui_core.R.style.ModalPage_Animation)
        setupActions()
        setOnClickListeners()
        observeData()
    }

    private fun setupActions() = with(binding) {
        actions = mapOf(
            sortByUserName.id to SortingUsersOptionsMenuActions.OrderByUserName,
            sortByUserLastName.id to SortingUsersOptionsMenuActions.OrderByUserLastName,
            sortByMoreBooks.id to SortingUsersOptionsMenuActions.OrderByMoreBooks,
            sortByMoreChapters.id to SortingUsersOptionsMenuActions.OrderByMoreChapters,
            sortByMorePages.id to SortingUsersOptionsMenuActions.OrderByMorePages,
        )
    }

    private fun observeData() = with(viewModel) {
        launchWhenCreated {
            internalSelection.observe(::updateSelectionMark)
        }
    }

    override fun onClick(view: View) {
        if (actions == null) return
        actions!![view.id]?.let(viewModel::orderUsers)
        dismiss()
    }

    private fun setOnClickListeners() = with(binding) {
        val listener = this@FragmentUsersSortDialog
        sortByUserName.setOnClickListener(listener)
        sortByUserLastName.setOnClickListener(listener)
        sortByMoreBooks.setOnClickListener(listener)
        sortByMoreChapters.setOnClickListener(listener)
        sortByMorePages.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingUsersOptionsMenuActions) = with(binding) {
        userNameConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByUserName
        userLastNameConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByUserLastName
        moreBooksConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByMoreBooks
        moreChaptersConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByMoreChapters
        morePagesConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByMorePages
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}