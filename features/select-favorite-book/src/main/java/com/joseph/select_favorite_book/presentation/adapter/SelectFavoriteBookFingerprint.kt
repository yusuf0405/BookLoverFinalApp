package com.joseph.select_favorite_book.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.select_favorite_book.R
import com.joseph.select_favorite_book.databinding.ItemSelectFavoriteBookBinding
import com.joseph.select_favorite_book.presentation.adapter.models.SelectFavoriteBookItem
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.core.extensions.showImage


internal class SelectFavoriteBookFingerprint :
    ItemFingerprint<ItemSelectFavoriteBookBinding, SelectFavoriteBookItem> {

    override fun isRelativeItem(item: Item) = item is SelectFavoriteBookItem

    override fun getLayoutId() = R.layout.item_select_favorite_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSelectFavoriteBookBinding, SelectFavoriteBookItem> {
        val binding = ItemSelectFavoriteBookBinding.inflate(layoutInflater, parent, false)
        return ExclusiveBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<SelectFavoriteBookItem>() {

        override fun areItemsTheSame(
            oldItem: SelectFavoriteBookItem,
            newItem: SelectFavoriteBookItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SelectFavoriteBookItem,
            newItem: SelectFavoriteBookItem
        ) = oldItem == newItem
    }

}

class ExclusiveBookViewHolder(
    binding: ItemSelectFavoriteBookBinding,
) : BaseViewHolder<ItemSelectFavoriteBookBinding, SelectFavoriteBookItem>(binding) {

    override fun onBind(item: SelectFavoriteBookItem) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        itemView.context.showImage(item.posterUrl, posterBackground)
    }

}

