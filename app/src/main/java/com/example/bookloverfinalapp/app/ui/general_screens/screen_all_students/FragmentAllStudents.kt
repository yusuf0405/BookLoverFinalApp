package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students

import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.AllItemsFetchType
import com.example.bookloverfinalapp.app.base.BaseFragmentAllItems
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.sort_dialog.FragmentUsersSortDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.HorizontalUserFingerprint
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllStudents : BaseFragmentAllItems() {

    private val userAdapter = FingerprintAdapter(
        listOf(
            SearchFingerprint(),
            HorizontalUserFingerprint()
        )
    )

    override val allItemsFetchType: AllItemsFetchType
        get() = AllItemsFetchType.ALL_USERS

    override val adapter: FingerprintAdapter
        get() = userAdapter

    override val layoutManager: LinearLayoutManager
        get() = GridLayoutManager(requireContext(), 2)

    override val toolbarTitle: Int
        get() = R.string.my_students

    override val sortDialogFragment: DialogFragment
        get() = FragmentUsersSortDialog.newInstance()
}