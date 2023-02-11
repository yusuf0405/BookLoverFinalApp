package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.transition.Transition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.SavedStatus
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ItemBookExclusiveBinding
import com.example.bookloverfinalapp.databinding.ItemHorizontalBookBinding


class ExclusiveBookFingerprint :
    ItemFingerprint<ItemBookExclusiveBinding, HorizontalBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is HorizontalBookAdapterModel

    override fun getLayoutId() = R.layout.item_book_exclusive

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBookExclusiveBinding, HorizontalBookAdapterModel> {
        val binding = ItemBookExclusiveBinding.inflate(layoutInflater, parent, false)
        return ExclusiveBookViewHolder(binding)
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

class ExclusiveBookViewHolder(
    binding: ItemBookExclusiveBinding,
) : BaseViewHolder<ItemBookExclusiveBinding, HorizontalBookAdapterModel>(binding) {

    override fun onBind(item: HorizontalBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        itemView.context.showRoundedImage(
            imageUrl = item.posterUrl,
            imageView = poster
        )
        title.text = item.title
        description.text = item.description

        Glide.with(itemView.context)
            .asBitmap()
            .load(item.posterUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?,
                ) { createPaletteAsync(resource) }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listener.bookItemOnClick(item.id) }
    }

    private fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val color = when {
                palette?.lightVibrantSwatch != null -> palette.lightVibrantSwatch!!.rgb
                palette?.lightMutedSwatch != null -> palette.lightMutedSwatch!!.rgb
                palette?.vibrantSwatch != null -> palette.vibrantSwatch!!.rgb
                palette?.mutedSwatch != null -> palette.mutedSwatch!!.rgb
                else -> itemView.context.getColor(R.color.rating_second_color)
            }
            binding.backgroundColor.setBackgroundColor(color)
        }
    }

}

