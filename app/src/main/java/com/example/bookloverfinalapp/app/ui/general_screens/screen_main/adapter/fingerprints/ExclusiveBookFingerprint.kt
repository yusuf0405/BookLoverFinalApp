package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.transition.Transition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.joseph.ui.core.R as UiCore
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.ExclusiveBookItem
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ItemBookExclusiveBinding
import com.joseph.core.extensions.showImage
import com.example.bookloverfinalapp.R

class ExclusiveBookFingerprint :
    ItemFingerprint<ItemBookExclusiveBinding, ExclusiveBookItem> {

    override fun isRelativeItem(item: Item) = item is ExclusiveBookItem

    override fun getLayoutId() = R.layout.item_book_exclusive

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBookExclusiveBinding, ExclusiveBookItem> {
        val binding = ItemBookExclusiveBinding.inflate(layoutInflater, parent, false)
        return ExclusiveBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ExclusiveBookItem>() {

        override fun areItemsTheSame(
            oldItem: ExclusiveBookItem,
            newItem: ExclusiveBookItem
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ExclusiveBookItem,
            newItem: ExclusiveBookItem
        ) = oldItem == newItem
    }

}

class ExclusiveBookViewHolder(
    binding: ItemBookExclusiveBinding,
) : BaseViewHolder<ItemBookExclusiveBinding, ExclusiveBookItem>(binding) {

    override fun onBind(item: ExclusiveBookItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        itemView.context.showImage(item.posterUrl, poster)
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
        root.setOnDownEffectClickListener { item.listener.bookItemOnClick(item.id) }
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val color = when {
                palette?.dominantSwatch != null -> palette.dominantSwatch!!.rgb
                palette?.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
                palette?.lightMutedSwatch != null -> palette.lightMutedSwatch!!.rgb
                palette?.vibrantSwatch != null -> palette.vibrantSwatch!!.rgb
                palette?.mutedSwatch != null -> palette.mutedSwatch!!.rgb
                palette?.darkVibrantSwatch != null -> palette.darkVibrantSwatch!!.rgb
                else -> itemView.context.getColor(UiCore.color.rating_second_color)
            }
            binding.backgroundColor.setBackgroundColor(color)
        }
    }

}

