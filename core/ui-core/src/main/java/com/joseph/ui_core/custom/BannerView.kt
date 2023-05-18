package com.joseph.ui_core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.Exception

class BannerView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {



    init {
        visibility = View.GONE
    }

    fun configure(

    ) {
        initView()
    }


    private fun initView() {
        visibility = View.VISIBLE
        setView()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    private fun setView() {

    }

    private fun resetLayoutParams() {
        (layoutParams as MarginLayoutParams).apply {
            background = null
            setPadding(0, 0, 0, 0)
            setMargins(0, 0, 0, 0)
            elevation = 0F
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}