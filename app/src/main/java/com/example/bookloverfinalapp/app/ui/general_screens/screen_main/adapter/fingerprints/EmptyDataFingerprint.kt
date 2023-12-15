package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.R
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.EmptyDataItem
import com.joseph.ui.core.databinding.ItemEmptyDataBinding


class EmptyDataFingerprint : ItemFingerprint<ItemEmptyDataBinding, EmptyDataItem> {

    override fun isRelativeItem(item: Item) = item is EmptyDataItem

    override fun getLayoutId() = R.layout.item_empty_data

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemEmptyDataBinding, EmptyDataItem> {
        val binding = ItemEmptyDataBinding.inflate(layoutInflater, parent, false)
        return EmptyDataViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<EmptyDataItem>() {

        override fun areItemsTheSame(oldItem: EmptyDataItem, newItem: EmptyDataItem) = false

        override fun areContentsTheSame(oldItem: EmptyDataItem, newItem: EmptyDataItem) = false
    }

}

class EmptyDataViewHolder(
    binding: ItemEmptyDataBinding,
) : BaseViewHolder<ItemEmptyDataBinding, EmptyDataItem>(binding) {

}

