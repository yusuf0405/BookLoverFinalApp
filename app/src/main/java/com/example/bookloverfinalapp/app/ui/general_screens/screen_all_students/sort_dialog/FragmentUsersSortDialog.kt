package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.databinding.FragmentUserSortDialogBinding
import com.joseph.common_api.base.BaseBindingFragment
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.extensions.launchWhenCreated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentUsersSortDialog :
    BaseBindingFragment<FragmentUserSortDialogBinding>(FragmentUserSortDialogBinding::inflate),
    View.OnClickListener {

    private val viewModel: SortingUsersOptionsViewModel by viewModels()

    private var actions: Map<Int, SortingUsersOptionsMenuActions>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        setOnClickListeners()
        observeData()
    }

    private fun setupActions() = with(binding()) {
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
        dismissModalPage()
    }

    private fun setOnClickListeners() = with(binding()) {
        val listener = this@FragmentUsersSortDialog
        sortByUserName.setOnClickListener(listener)
        sortByUserLastName.setOnClickListener(listener)
        sortByMoreBooks.setOnClickListener(listener)
        sortByMoreChapters.setOnClickListener(listener)
        sortByMorePages.setOnClickListener(listener)
    }

    private fun updateSelectionMark(action: SortingUsersOptionsMenuActions) = with(binding()) {
        userNameConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByUserName
        userLastNameConnectedMark.isVisible =
            action == SortingUsersOptionsMenuActions.OrderByUserLastName
        moreBooksConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByMoreBooks
        moreChaptersConnectedMark.isVisible =
            action == SortingUsersOptionsMenuActions.OrderByMoreChapters
        morePagesConnectedMark.isVisible = action == SortingUsersOptionsMenuActions.OrderByMorePages
    }

    companion object {
        fun newInstance() = FragmentUsersSortDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}