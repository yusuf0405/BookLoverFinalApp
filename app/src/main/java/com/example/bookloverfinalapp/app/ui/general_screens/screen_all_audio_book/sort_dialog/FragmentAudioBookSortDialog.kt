package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.databinding.FragmentSavedBookSortDialogBinding
import com.joseph.common_api.base.BaseBindingFragment
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAudioBookSortDialog :
    BaseBindingFragment<FragmentSavedBookSortDialogBinding>(FragmentSavedBookSortDialogBinding::inflate),
    View.OnClickListener {

    private val viewModel: SortingAudioBookOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingAudioBookOptionsMenuActions>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setOnClickListeners()
        observeData()
        binding().sortByReading.isVisible = false
    }

    private fun setupActions() = with(binding()) {
        actions = mapOf(
            sortByDate.id to SortingAudioBookOptionsMenuActions.OrderByDate,
            sortByBookAuthor.id to SortingAudioBookOptionsMenuActions.OrderByAuthorName,
            sortByBookName.id to SortingAudioBookOptionsMenuActions.OrderByUsersSortName,
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
        val listener = this@FragmentAudioBookSortDialog
        sortByDate.setOnClickListener(listener)
        sortByBookName.setOnClickListener(listener)
        sortByBookAuthor.setOnClickListener(listener)
        sortByReading.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingAudioBookOptionsMenuActions) = with(binding()) {
        dateConnectedMark.isVisible = action == SortingAudioBookOptionsMenuActions.OrderByDate
        bookAuthorConnectedMark.isVisible =
            action == SortingAudioBookOptionsMenuActions.OrderByAuthorName
        bookNameConnectedMark.isVisible =
            action == SortingAudioBookOptionsMenuActions.OrderByUsersSortName
    }

    companion object {
        fun newInstance() = FragmentAudioBookSortDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }

}