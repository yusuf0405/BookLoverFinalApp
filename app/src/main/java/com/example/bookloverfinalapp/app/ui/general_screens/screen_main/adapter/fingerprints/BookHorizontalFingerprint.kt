package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.SavedStatus
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ItemHorizontalBookBinding
import com.joseph.utils_core.extensions.showRoundedImage


class BookHorizontalFingerprint :
    ItemFingerprint<ItemHorizontalBookBinding, HorizontalBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is HorizontalBookAdapterModel

    override fun getLayoutId() = R.layout.item_horizontal_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemHorizontalBookBinding, HorizontalBookAdapterModel> {
        val binding = ItemHorizontalBookBinding.inflate(layoutInflater, parent, false)
        return BookViewHorizontalHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalBookAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: HorizontalBookAdapterModel,
            newItem: HorizontalBookAdapterModel
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HorizontalBookAdapterModel,
            newItem: HorizontalBookAdapterModel
        ) =
            oldItem == newItem
    }

}

class BookViewHorizontalHolder(
    binding: ItemHorizontalBookBinding,
) : BaseViewHolder<ItemHorizontalBookBinding, HorizontalBookAdapterModel>(binding) {

    override fun onBind(item: HorizontalBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        buttonBlock.show()
        infoBlock.hide()
        itemView.context.showRoundedImage(
            imageUrl = item.posterUrl,
            imageView = bookPoster
        )
        when (item.savedStatus) {
            SavedStatus.SAVED -> downloadedMark.show()
            SavedStatus.NOT_SAVED -> downloadedMark.hide()
            SavedStatus.SAVING -> Unit
        }
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listener.bookItemOnClick(item.id) }
        button.setOnDownEffectClickListener { item.listener.bookItemOnClick(item.id) }
        moreButton.setOnDownEffectClickListener {
            item.listener.bookOptionMenuOnClick(bookId = item.id)
        }
    }
}

