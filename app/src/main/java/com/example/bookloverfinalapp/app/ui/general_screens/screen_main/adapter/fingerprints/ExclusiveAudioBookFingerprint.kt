package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.ExclusiveAudioBookItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemExclusiveAudioBookBinding


class ExclusiveAudioBookFingerprint :
    ItemFingerprint<ItemExclusiveAudioBookBinding, ExclusiveAudioBookItem> {

    override fun isRelativeItem(item: Item) = item is ExclusiveAudioBookItem

    override fun getLayoutId() = R.layout.item_exclusive_audio_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemExclusiveAudioBookBinding, ExclusiveAudioBookItem> {
        val binding = ItemExclusiveAudioBookBinding.inflate(layoutInflater, parent, false)
        return ExclusiveAudioBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ExclusiveAudioBookItem>() {

        override fun areItemsTheSame(
            oldItem: ExclusiveAudioBookItem,
            newItem: ExclusiveAudioBookItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ExclusiveAudioBookItem,
            newItem: ExclusiveAudioBookItem
        ) = oldItem == newItem
    }

}

class ExclusiveAudioBookViewHolder(
    binding: ItemExclusiveAudioBookBinding,
) : BaseViewHolder<ItemExclusiveAudioBookBinding, ExclusiveAudioBookItem>(binding) {

    override fun onBind(item: ExclusiveAudioBookItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        itemView.context.showImage(item.posterUrl, posterImageView)
        title.text = item.title
        description.text = item.description
        Glide.with(itemView.context)
            .asBitmap()
            .load(item.posterUrl)
            .into(createCustomTarget())
    }

    private fun createCustomTarget() = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            createPaletteAsync(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listener.audioBookItemOnClick(item.id) }
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val color = when {
                palette?.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
                palette?.dominantSwatch != null -> palette.dominantSwatch!!.rgb
                palette?.lightMutedSwatch != null -> palette.lightMutedSwatch!!.rgb
                palette?.vibrantSwatch != null -> palette.vibrantSwatch!!.rgb
                palette?.mutedSwatch != null -> palette.mutedSwatch!!.rgb
                palette?.darkVibrantSwatch != null -> palette.darkVibrantSwatch!!.rgb
                else -> itemView.context.getColor(R.color.rating_second_color)
            }
            binding.backgroundColor.setBackgroundColor(color)
        }
    }
}

