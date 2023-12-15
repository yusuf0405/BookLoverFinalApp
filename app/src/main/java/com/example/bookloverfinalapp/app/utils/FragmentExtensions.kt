package com.example.bookloverfinalapp.app.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.ModalPageNavigationTransition
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.ModalPageFragment

fun Fragment.modalPageNavigateTo(
    newFragment: Fragment,
    title: String,
    rootContainer: View,
    transitionName: String,
) {
    val fragment = parentFragment as ModalPageFragment
    fragment.setTitle(title)
    parentFragmentManager.beginTransaction()
        .apply {
            setReorderingAllowed(true)
            addSharedElement(rootContainer, transitionName)
            newFragment.sharedElementEnterTransition = ModalPageNavigationTransition()
        }
        .replace(R.id.container, newFragment)
        .addToBackStack(ModalPage.TAG)
        .commit()
}