package com.example.bookloverfinalapp.app.custom

import android.content.Context
import kotlinx.coroutines.Job

interface ItemUi {

    fun type(): Int

    fun id(): String

    fun show(vararg views: CustomView, context: Context, position: Int)
}

interface CustomView {

    fun show(text: String) = Unit

    fun show(textId: Int) = Unit

    fun loadImage(url: String) = Unit

    fun visibility(status: Boolean) = Unit

    fun handleClick(function: () -> Job) = Unit
}