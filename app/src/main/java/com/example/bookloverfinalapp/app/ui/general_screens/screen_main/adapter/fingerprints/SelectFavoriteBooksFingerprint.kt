package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.SelectFavoriteBooksItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemAddStoriesBinding
import com.example.bookloverfinalapp.databinding.ItemMainSelectFavoriteBookBinding
import com.joseph.stories.presentation.models.StoriesAddAdapterItem
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint


class SelectFavoriteBooksFingerprint :
    ItemFingerprint<ItemMainSelectFavoriteBookBinding, SelectFavoriteBooksItem> {

    override fun isRelativeItem(item: Item) = item is SelectFavoriteBooksItem

    override fun getLayoutId() = R.layout.item_main_select_favorite_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMainSelectFavoriteBookBinding, SelectFavoriteBooksItem> {
        val binding = ItemMainSelectFavoriteBookBinding.inflate(layoutInflater, parent, false)
        return SelectFavoriteBooksViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil =
        object : DiffUtil.ItemCallback<SelectFavoriteBooksItem>() {

            override fun areItemsTheSame(
                oldItem: SelectFavoriteBooksItem,
                newItem: SelectFavoriteBooksItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SelectFavoriteBooksItem,
                newItem: SelectFavoriteBooksItem
            ) = oldItem == newItem
        }

}

class SelectFavoriteBooksViewHolder(
    binding: ItemMainSelectFavoriteBookBinding,
) : BaseViewHolder<ItemMainSelectFavoriteBookBinding, SelectFavoriteBooksItem>(binding) {

    override fun onBind(item: SelectFavoriteBooksItem) {
        super.onBind(item)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        selectButton.setOnDownEffectClickListener {
           item.listener.onClickSelectFavoriteBook()
        }
    }
}

