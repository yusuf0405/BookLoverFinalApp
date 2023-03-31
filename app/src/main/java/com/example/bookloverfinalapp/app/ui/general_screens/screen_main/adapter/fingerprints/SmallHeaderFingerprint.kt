package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.databinding.ItemSmallHeaderBinding

class SmallHeaderFingerprint : ItemFingerprint<ItemSmallHeaderBinding, HeaderItem> {

    override fun isRelativeItem(item: Item) = item is HeaderItem

    override fun getLayoutId() = R.layout.item_small_header

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): SmallHeaderViewHolder {
        val binding = ItemSmallHeaderBinding.inflate(layoutInflater, parent, false)
        return SmallHeaderViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HeaderItem>() {

        override fun areItemsTheSame(oldItem: HeaderItem, newItem: HeaderItem) = true

        override fun areContentsTheSame(oldItem: HeaderItem, newItem: HeaderItem) = true
    }

}

class SmallHeaderViewHolder(
    binding: ItemSmallHeaderBinding,
) : BaseViewHolder<ItemSmallHeaderBinding, HeaderItem>(binding) {

    override fun onBind(item: HeaderItem) {
        super.onBind(item)
        binding.title.setText(item.titleId.id)
    }
}
