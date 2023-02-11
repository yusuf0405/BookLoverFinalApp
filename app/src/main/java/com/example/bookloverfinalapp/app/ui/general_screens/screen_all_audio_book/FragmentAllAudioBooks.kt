package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog.FragmentAudioBookSortDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AudioBookFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint

class FragmentAllAudioBooks : BaseFragmentAllItems() {

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_AUDIO_BOOKS

    override val toolbarTitle: Int
        get() = R.string.all_audio_books

    override val sortDialogFragment: DialogFragment
        get() = FragmentAudioBookSortDialog()

    override val layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(requireContext())

    override val adapter: FingerprintAdapter =
        FingerprintAdapter(listOf(SearchFingerprint(), AudioBookFingerprint()))
}