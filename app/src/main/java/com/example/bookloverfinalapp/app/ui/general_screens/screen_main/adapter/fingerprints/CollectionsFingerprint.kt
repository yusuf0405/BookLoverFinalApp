package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.adapter.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.CollectionItem
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemCollectionsBinding
import com.example.bookloverfinalapp.R

class CollectionsFingerprint : ItemFingerprint<ItemCollectionsBinding, CollectionItem> {

    override fun isRelativeItem(item: Item) = item is CollectionItem

    override fun getLayoutId() = R.layout.item_collections

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemCollectionsBinding, CollectionItem> {
        val binding = ItemCollectionsBinding.inflate(layoutInflater, parent, false)
        return CollectionViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<CollectionItem>() {

        override fun areItemsTheSame(
            oldItem: CollectionItem,
            newItem: CollectionItem
        ) = oldItem.collections.title == newItem.collections.title

        override fun areContentsTheSame(
            oldItem: CollectionItem,
            newItem: CollectionItem
        ) = oldItem == newItem
    }

}

class CollectionViewHolder(
    binding: ItemCollectionsBinding,
) : BaseViewHolder<ItemCollectionsBinding, CollectionItem>(binding) {

    override fun onBind(item: CollectionItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        title.text = itemView.context.getText(item.collections.title.id)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listener.collectionItemOnClick(item.collections) }
    }

}

