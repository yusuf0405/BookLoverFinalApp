package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.sort_dialog.FragmentSavedBookSortDialog
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookGenreFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllGenres : BaseFragmentAllItems() {

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_GENRES

    override val toolbarTitle: Int
        get() = R.string.genres

    override val sortDialogFragment: DialogFragment
        get() = FragmentSavedBookSortDialog.newInstance()

    override val layoutManager: LinearLayoutManager
        get() = GridLayoutManager(requireContext(), 2)

    override val adapter = FingerprintAdapter(
        listOf(
            SearchFingerprint(),
            BookGenreFingerprint(),
        )
    )

}