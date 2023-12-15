package com.joseph.stories.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.joseph.core.extensions.getDpFromPixel
import com.joseph.core.extensions.getPixelFromDp

abstract class CommonViewHolder(private val view: View) :
    RecyclerView.ViewHolder(view) {

    lateinit var onViewClick: ((View) -> Unit)

    abstract fun initializeView(model: Any)

    fun getPadding(): RecyclerRowPadding {
        val params = itemView.layoutParams as RecyclerView.LayoutParams
        return RecyclerRowPadding(
            getDp(params.leftMargin),
            getDp(params.topMargin),
            getDp(params.rightMargin),
            getDp(params.bottomMargin)
        )
    }

    fun setPadding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
        val params = itemView.layoutParams as RecyclerView.LayoutParams
        start?.let {
            params.leftMargin = getPixel(it)
        }
        top?.let {
            params.topMargin = getPixel(it)
        }
        end?.let {
            params.rightMargin = getPixel(it)
        }
        bottom?.let {
            params.bottomMargin = getPixel(it)
        }
        view.layoutParams = params
    }

    private fun getPixel(value:Int): Int = view.getPixelFromDp(value).toInt()

    private fun getDp(value:Int): Int = view.getDpFromPixel(value).toInt()
}