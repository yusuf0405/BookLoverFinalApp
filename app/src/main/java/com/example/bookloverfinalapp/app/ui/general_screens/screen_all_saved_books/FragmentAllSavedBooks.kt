package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog.FragmentSavedBookSortDialog
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint

class FragmentAllSavedBooks : BaseFragmentAllItems() {

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_SAVED_BOOKS

    override val toolbarTitle: Int
        get() = R.string.saved_books

    override val sortDialogFragment: DialogFragment
        get() = FragmentSavedBookSortDialog.newInstance()

    override val layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(requireContext())

    override val adapter: FingerprintAdapter =
        FingerprintAdapter(listOf(SearchFingerprint(), SavedBookFingerprint()))
}