package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online.fingerprint

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.PhotoType
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.PosterItem
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemPosterBinding
import java.io.File


class PosterFingerprint : ItemFingerprint<ItemPosterBinding, PosterItem> {

    override fun isRelativeItem(item: Item) = item is PosterItem

    override fun getLayoutId() = R.layout.item_poster

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemPosterBinding, PosterItem> {
        val binding = ItemPosterBinding.inflate(layoutInflater, parent, false)
        return PosterViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<PosterItem>() {

        override fun areItemsTheSame(oldItem: PosterItem, newItem: PosterItem) =
            oldItem.posterUrl == newItem.posterUrl

        override fun areContentsTheSame(
            oldItem: PosterItem,
            newItem: PosterItem
        ) = oldItem == newItem
    }

}

class PosterViewHolder(
    binding: ItemPosterBinding,
) : BaseViewHolder<ItemPosterBinding, PosterItem>(binding) {

    override fun onBind(item: PosterItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        val context = itemView.context
        when (item.type) {
            PhotoType.NETWORK -> context.showImage(item.posterUrl, posterImageView)
            PhotoType.LOCAL -> context.showImage(File(item.posterFileUri), posterImageView)
        }
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            when (item.type) {
                PhotoType.NETWORK -> item.onClickListener(item.posterUrl)
                PhotoType.LOCAL -> item.onClickListener(item.posterFileUri)
            }
        }
    }
}

