package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.SavedStatus
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ItemHorizontalBookBinding
import com.joseph.ui_core.extensions.toDp


class BookHorizontalSmallFingerprint :
    ItemFingerprint<ItemHorizontalBookBinding, HorizontalBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is HorizontalBookAdapterModel

    override fun getLayoutId() = R.layout.item_horizontal_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemHorizontalBookBinding, HorizontalBookAdapterModel> {
        val binding = ItemHorizontalBookBinding.inflate(layoutInflater, parent, false)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newLayoutParams.setMargins(10.toDp, 4.toDp, 8.toDp, 10.toDp)
        binding.root.layoutParams = newLayoutParams
        binding.root.layoutParams = binding.root.layoutParams.also {
            it.width = 120.toDp
            it.height = 190.toDp
        }
        return BookViewHorizontalSmallHolder(binding)
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

class BookViewHorizontalSmallHolder(
    binding: ItemHorizontalBookBinding,
) : BaseViewHolder<ItemHorizontalBookBinding, HorizontalBookAdapterModel>(binding) {

    override fun onBind(item: HorizontalBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        buttonBlock.hide()
        infoBlock.hide()
        title.text = item.title
        author.text = item.author
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
    }
}

