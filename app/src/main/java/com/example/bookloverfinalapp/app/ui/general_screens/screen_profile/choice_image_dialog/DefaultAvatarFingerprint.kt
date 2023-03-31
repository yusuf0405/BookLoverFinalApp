package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemDefaultAvatarBinding


class DefaultAvatarFingerprint : ItemFingerprint<ItemDefaultAvatarBinding, DefaultAvatarItem> {

    override fun isRelativeItem(item: Item) = item is DefaultAvatarItem

    override fun getLayoutId() = R.layout.item_default_avatar

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemDefaultAvatarBinding, DefaultAvatarItem> {
        val binding = ItemDefaultAvatarBinding.inflate(layoutInflater, parent, false)
        return DefaultAvatarViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<DefaultAvatarItem>() {

        override fun areItemsTheSame(
            oldItem: DefaultAvatarItem,
            newItem: DefaultAvatarItem
        ) = oldItem.imageResource == newItem.imageResource

        override fun areContentsTheSame(
            oldItem: DefaultAvatarItem,
            newItem: DefaultAvatarItem
        ) = oldItem == newItem
    }
}

class DefaultAvatarViewHolder(
    binding: ItemDefaultAvatarBinding,
) : BaseViewHolder<ItemDefaultAvatarBinding, DefaultAvatarItem>(binding) {

    override fun onBind(item: DefaultAvatarItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        itemView.context.showImage(item.imageResource, avatar)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.defaultAvatarOnClickListener(item.imageResource)
        }
    }

}

