package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemStoriesBinding
import com.joseph.stories.presentation.models.StoriesAdapterItem
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.core.extensions.showImage
import com.example.bookloverfinalapp.R

class StoriesFingerprint :
    ItemFingerprint<ItemStoriesBinding, StoriesAdapterItem> {

    override fun isRelativeItem(item: Item) = item is StoriesAdapterItem

    override fun getLayoutId() = R.layout.item_stories

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemStoriesBinding, StoriesAdapterItem> {
        val binding = ItemStoriesBinding.inflate(layoutInflater, parent, false)
        return StoriesViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil =
        object : DiffUtil.ItemCallback<StoriesAdapterItem>() {

            override fun areItemsTheSame(
                oldItem: StoriesAdapterItem,
                newItem: StoriesAdapterItem
            ) =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(
                oldItem: StoriesAdapterItem,
                newItem: StoriesAdapterItem
            ) = oldItem == newItem
        }

}

class StoriesViewHolder(
    binding: ItemStoriesBinding,
) : BaseViewHolder<ItemStoriesBinding, StoriesAdapterItem>(binding) {

    override fun onBind(item: StoriesAdapterItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        val context = itemView.context
        name.text = item.name
        context.showImage(item.previewImageUrl, userAvatar)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.storiesOnClickListener(adapterPosition - 1)
        }
    }
}

