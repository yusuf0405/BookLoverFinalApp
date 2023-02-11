package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

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
import com.example.bookloverfinalapp.databinding.FragmentBookSortDialogBinding
import com.joseph.ui_core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentBookSortDialog : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentBookSortDialogBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: SortingBookOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingBookOptionsMenuActions>? = null

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
        _binding = FragmentBookSortDialogBinding.inflate(inflater)
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
            sortByDate.id to SortingBookOptionsMenuActions.OrderByDate,
            sortByBookAuthor.id to SortingBookOptionsMenuActions.OrderByAuthorName,
            sortByBookName.id to SortingBookOptionsMenuActions.OrderByBookName,
            sortByCached.id to SortingBookOptionsMenuActions.OrderByCached,
        )
    }

    private fun observeData() = with(viewModel) {
        launchWhenCreated {
            internalSelection.observe(::updateSelectionMark)
        }
    }

    override fun onClick(view: View) {
        if (actions == null) return
        actions!![view.id]?.let(viewModel::orderBooks)
        dismiss()
    }

    private fun setOnClickListeners() = with(binding) {
        val listener = this@FragmentBookSortDialog
        sortByDate.setOnClickListener(listener)
        sortByBookName.setOnClickListener(listener)
        sortByBookAuthor.setOnClickListener(listener)
        sortByCached.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingBookOptionsMenuActions) = with(binding) {
        dateConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByDate
        bookAuthorConnectedMark.isVisible =
            action == SortingBookOptionsMenuActions.OrderByAuthorName
        bookNameConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByBookName
        byCachedConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByCached
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}