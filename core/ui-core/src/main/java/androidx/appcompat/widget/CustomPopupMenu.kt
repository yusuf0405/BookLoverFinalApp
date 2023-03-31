package androidx.appcompat.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.StyleRes

@SuppressLint("RestrictedApi")
class CustomPopupMenu(
    context: Context,
    view: View,
    gravity: Int,
    @StyleRes styleRes: Int,
) : PopupMenu(context, view, gravity, 0, styleRes) {

    init {
        mPopup.setForceShowIcon(true)
    }
}