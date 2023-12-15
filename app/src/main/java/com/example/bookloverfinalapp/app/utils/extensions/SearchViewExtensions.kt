package com.example.bookloverfinalapp.app.utils.extensions

import android.util.TypedValue
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import com.joseph.ui.core.R

fun SearchView.setupSearchViewParams() {
    val searchText = this.findViewById<View>(
        this.context.resources.getIdentifier(
            "android:id/search_src_text",
            null,
            null
        )
    ) as AutoCompleteTextView
    searchText.setTextSize(
        TypedValue.COMPLEX_UNIT_PX,
        resources.getDimensionPixelSize(R.dimen.textSizeForSmallTexts).toFloat()
    )
    val typeface = ResourcesCompat.getFont(this.context, R.font.poppins_regular)
    searchText.typeface = typeface
}