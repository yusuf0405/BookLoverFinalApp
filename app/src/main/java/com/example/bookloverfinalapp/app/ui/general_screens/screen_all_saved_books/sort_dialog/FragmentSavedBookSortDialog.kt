package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.tuneBottomDialog
import com.example.bookloverfinalapp.app.utils.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.FragmentSavedBookSortDialogBinding
import com.joseph.ui_core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSavedBookSortDialog : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentSavedBookSortDialogBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: SortingSavedBookOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingSavedBookOptionsMenuActions>? = null

    override fun onStart() {
        super.onStart()
        tuneBottomDialog()
//        tuneLyricsDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBookSortDialogBinding.inflate(inflater)
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
            sortByDate.id to SortingSavedBookOptionsMenuActions.OrderByDate,
            sortByBookAuthor.id to SortingSavedBookOptionsMenuActions.OrderByAuthorName,
            sortByBookName.id to SortingSavedBookOptionsMenuActions.OrderByUsersSortName,
            sortByReading.id to SortingSavedBookOptionsMenuActions.OrderByReading,
        )
    }

    private fun observeData() = with(viewModel) {
        launchWhenCreated {
            internalSelection.observe(::updateSelectionMark)
        }
    }

    override fun onClick(view: View) {
        if (actions == null) return
        actions!![view.id]?.let(viewModel::orderSavedBooks)
        dismiss()
    }

    private fun setOnClickListeners() = with(binding) {
        val listener = this@FragmentSavedBookSortDialog
        sortByDate.setOnClickListener(listener)
        sortByBookName.setOnClickListener(listener)
        sortByBookAuthor.setOnClickListener(listener)
        sortByReading.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingSavedBookOptionsMenuActions) = with(binding) {
        dateConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByDate
        bookAuthorConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByAuthorName
        bookNameConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByUsersSortName
        readingConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByReading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}