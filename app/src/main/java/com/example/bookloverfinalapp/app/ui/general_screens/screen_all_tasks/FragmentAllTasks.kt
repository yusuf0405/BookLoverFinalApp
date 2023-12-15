package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_tasks

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_audio_book.sort_dialog.FragmentAudioBookSortDialog
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint

class FragmentAllTasks : BaseFragmentAllItems() {

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_TASKS

    override val adapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            TaskAllItemFingerprint()
        )
    )

    override val toolbarTitle: Int
        get() = R.string.all_tasks

    override val layoutManager: LinearLayoutManager
        get() = GridLayoutManager(requireContext(), 2)

    override val sortDialogFragment: DialogFragment
        get() =  FragmentAudioBookSortDialog.newInstance()

}