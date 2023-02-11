package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding


interface Item

interface ItemFingerprint<V : ViewBinding, I : Item> {

    fun isRelativeItem(item: Item): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<V, I>

    fun getDiffUtil(): DiffUtil.ItemCallback<I>

}