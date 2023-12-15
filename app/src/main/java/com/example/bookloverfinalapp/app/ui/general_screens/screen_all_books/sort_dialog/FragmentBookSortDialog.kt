package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.databinding.FragmentBookSortDialogBinding
import com.joseph.common.base.BaseBindingFragment
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import com.joseph.ui.core.extensions.launchOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentBookSortDialog :
    BaseBindingFragment<FragmentBookSortDialogBinding>(FragmentBookSortDialogBinding::inflate),
    View.OnClickListener {

    private val viewModel: SortingBookOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingBookOptionsMenuActions>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setOnClickListeners()
        observeData()
    }

    private fun setupActions() = with(binding()) {
        actions = mapOf(
            sortByDate.id to SortingBookOptionsMenuActions.OrderByDate,
            sortByBookAuthor.id to SortingBookOptionsMenuActions.OrderByAuthorName,
            sortByBookName.id to SortingBookOptionsMenuActions.OrderByBookName,
            sortByCached.id to SortingBookOptionsMenuActions.OrderByCached,
        )
    }

    private fun observeData() = with(viewModel) {
        launchOnLifecycle {
            internalSelection.observe(::updateSelectionMark)
        }
    }

    override fun onClick(view: View) {
        if (actions == null) return
        actions!![view.id]?.let(viewModel::orderBooks)
        dismissModalPage()
    }

    private fun setOnClickListeners() = with(binding()) {
        val listener = this@FragmentBookSortDialog
        sortByDate.setOnClickListener(listener)
        sortByBookName.setOnClickListener(listener)
        sortByBookAuthor.setOnClickListener(listener)
        sortByCached.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingBookOptionsMenuActions) = with(binding()) {
        dateConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByDate
        bookAuthorConnectedMark.isVisible =
            action == SortingBookOptionsMenuActions.OrderByAuthorName
        bookNameConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByBookName
        byCachedConnectedMark.isVisible = action == SortingBookOptionsMenuActions.OrderByCached
    }

    companion object {
        fun newInstance() = FragmentBookSortDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}