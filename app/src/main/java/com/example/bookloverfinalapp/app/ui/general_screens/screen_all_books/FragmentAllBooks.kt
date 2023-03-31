package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.sort_dialog.FragmentBookSortDialog
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint

class FragmentAllBooks : BaseFragmentAllItems() {

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_BOOKS

    override val toolbarTitle: Int
        get() = R.string.all_books

    override val sortDialogFragment: DialogFragment
        get() = FragmentBookSortDialog()

    override val layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(requireContext())

    override val adapter = FingerprintAdapter(listOf(SearchFingerprint(), BookFingerprint()))
}