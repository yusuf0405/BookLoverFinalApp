package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.databinding.FragmentSavedBookSortDialogBinding
import com.joseph.common.base.BaseBindingFragment
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import com.joseph.ui.core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSavedBookSortDialog :
    BaseBindingFragment<FragmentSavedBookSortDialogBinding>(FragmentSavedBookSortDialogBinding::inflate),
    View.OnClickListener {

    private val viewModel: SortingSavedBookOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingSavedBookOptionsMenuActions>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setOnClickListeners()
        observeData()
    }

    private fun setupActions() = with(binding()) {
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
        dismissModalPage()
    }

    private fun setOnClickListeners() = with(binding()) {
        val listener = this@FragmentSavedBookSortDialog
        sortByDate.setOnClickListener(listener)
        sortByBookName.setOnClickListener(listener)
        sortByBookAuthor.setOnClickListener(listener)
        sortByReading.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingSavedBookOptionsMenuActions) = with(binding()) {
        dateConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByDate
        bookAuthorConnectedMark.isVisible =
            action == SortingSavedBookOptionsMenuActions.OrderByAuthorName
        bookNameConnectedMark.isVisible =
            action == SortingSavedBookOptionsMenuActions.OrderByUsersSortName
        readingConnectedMark.isVisible = action == SortingSavedBookOptionsMenuActions.OrderByReading
    }

    companion object {
        fun newInstance() = FragmentSavedBookSortDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}