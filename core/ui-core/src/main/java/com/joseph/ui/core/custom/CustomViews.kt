package com.joseph.ui.core.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job

class MyTextView : androidx.appcompat.widget.AppCompatTextView, CustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    override fun show(text: String) { setText(text) }

    override fun visibility(status: Boolean) { visibility = if (status) View.VISIBLE else View.GONE }

}

class MyImageView : androidx.appcompat.widget.AppCompatImageView, CustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    override fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .into(this)
    }

    override fun visibility(status: Boolean) {
        visibility = if (status) View.VISIBLE else View.GONE
    }

}

class MyButton : androidx.appcompat.widget.AppCompatButton, CustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr)

    override fun show(text: String) {
        setText(text)
    }

    override fun show(textId: Int) {
        setText(textId)
    }

    override fun handleClick(function: () -> Job) {
        setOnClickListener { function() }
    }

    override fun setTextColor(color: Int) {
        setTextColor(color)
    }

}


