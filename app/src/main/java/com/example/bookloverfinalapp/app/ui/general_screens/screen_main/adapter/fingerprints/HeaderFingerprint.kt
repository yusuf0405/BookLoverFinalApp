package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.joseph.ui_core.databinding.ItemHeaderBinding

class HeaderFingerprint : ItemFingerprint<ItemHeaderBinding, HeaderItem> {

    override fun isRelativeItem(item: Item) = item is HeaderItem

    override fun getLayoutId() = R.layout.item_header

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): HeaderViewHolder {
        val binding = ItemHeaderBinding.inflate(layoutInflater, parent, false)
        return HeaderViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HeaderItem>() {

        override fun areItemsTheSame(oldItem: HeaderItem, newItem: HeaderItem) = true

        override fun areContentsTheSame(oldItem: HeaderItem, newItem: HeaderItem) = true
    }

}

class HeaderViewHolder(
    binding: ItemHeaderBinding,
) : BaseViewHolder<ItemHeaderBinding, HeaderItem>(binding) {

    override fun onBind(item: HeaderItem) {
        super.onBind(item)
        with(binding) {
            container.startSlideInLeftAnim()
            seeMore.isVisible = item.showMoreIsVisible
            title.text = item.titleId.format(title.context)
            if (item.showMoreIsVisible) root.setOnDownEffectClickListener { item.onClickListener() }
        }
    }
}
