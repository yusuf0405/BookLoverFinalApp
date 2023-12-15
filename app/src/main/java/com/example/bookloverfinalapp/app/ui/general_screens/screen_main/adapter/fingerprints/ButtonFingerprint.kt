package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.GenreBlockButtonItem
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemBottomButtonBinding
import com.example.bookloverfinalapp.R

class ButtonFingerprint : ItemFingerprint<ItemBottomButtonBinding, GenreBlockButtonItem> {

    override fun isRelativeItem(item: Item) = item is GenreBlockButtonItem

    override fun getLayoutId() = R.layout.item_bottom_button

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ButtonViewHolder {
        val binding = ItemBottomButtonBinding.inflate(layoutInflater, parent, false)
        return ButtonViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<GenreBlockButtonItem>() {

        override fun areItemsTheSame(oldItem: GenreBlockButtonItem, newItem: GenreBlockButtonItem) =
            true

        override fun areContentsTheSame(
            oldItem: GenreBlockButtonItem,
            newItem: GenreBlockButtonItem
        ) = true
    }

}

class ButtonViewHolder(
    binding: ItemBottomButtonBinding,
) : BaseViewHolder<ItemBottomButtonBinding, GenreBlockButtonItem>(binding) {

    override fun onBind(item: GenreBlockButtonItem) {
        super.onBind(item)
        with(binding) {
            container.startSlideInLeftAnim()
            button.setOnDownEffectClickListener { item.onClickListener() }
        }
    }
}
