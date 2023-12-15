package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.MainScreenErrorItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemFailFullscreenBinding

class MainScreenErrorFingerprint :
    ItemFingerprint<ItemFailFullscreenBinding, MainScreenErrorItem> {

    override fun isRelativeItem(item: Item) = item is MainScreenErrorItem

    override fun getLayoutId() = R.layout.item_fail_fullscreen

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): MainScreenErrorViewHolder {
        val binding = ItemFailFullscreenBinding.inflate(layoutInflater, parent, false)
        return MainScreenErrorViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MainScreenErrorItem>() {

        override fun areItemsTheSame(
            oldItem: MainScreenErrorItem,
            newItem: MainScreenErrorItem
        ) = oldItem.errorMessage == newItem.errorMessage

        override fun areContentsTheSame(
            oldItem: MainScreenErrorItem,
            newItem: MainScreenErrorItem
        ) = oldItem == newItem
    }

}

class MainScreenErrorViewHolder(
    binding: ItemFailFullscreenBinding,
) : BaseViewHolder<ItemFailFullscreenBinding, MainScreenErrorItem>(binding) {

    override fun onBind(item: MainScreenErrorItem) {
        binding.messageTextView.text = item.errorMessage
        binding.tryAgain.setOnDownEffectClickListener { item.listener.tryAgain() }
    }
}
