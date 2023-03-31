package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemAddStoriesBinding
import com.joseph.stories.presentation.models.StoriesAddAdapterItem
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint


class AddStoriesFingerprint :
    ItemFingerprint<ItemAddStoriesBinding, StoriesAddAdapterItem> {

    override fun isRelativeItem(item: Item) = item is StoriesAddAdapterItem

    override fun getLayoutId() = R.layout.item_add_stories

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemAddStoriesBinding, StoriesAddAdapterItem> {
        val binding = ItemAddStoriesBinding.inflate(layoutInflater, parent, false)
        return StoriesAddViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil =
        object : DiffUtil.ItemCallback<StoriesAddAdapterItem>() {

            override fun areItemsTheSame(
                oldItem: StoriesAddAdapterItem,
                newItem: StoriesAddAdapterItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: StoriesAddAdapterItem,
                newItem: StoriesAddAdapterItem
            ) = oldItem == newItem
        }

}

class StoriesAddViewHolder(
    binding: ItemAddStoriesBinding,
) : BaseViewHolder<ItemAddStoriesBinding, StoriesAddAdapterItem>(binding) {

    override fun onBind(item: StoriesAddAdapterItem) {
        super.onBind(item)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
           item.listener.addStoriesOnClickListener()
        }
    }
}

