package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.MainScreenShimmerItem
import com.example.bookloverfinalapp.databinding.ItemAllBooksShimmerBinding

class MainScreenShimmerFingerprint :
    ItemFingerprint<ItemAllBooksShimmerBinding, MainScreenShimmerItem> {

    override fun isRelativeItem(item: Item) = item is MainScreenShimmerItem

    override fun getLayoutId() = R.layout.item_all_books_shimmer

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAllBooksShimmerViewHolder {
        val binding = ItemAllBooksShimmerBinding.inflate(layoutInflater, parent, false)
        return ItemAllBooksShimmerViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MainScreenShimmerItem>() {

        override fun areItemsTheSame(
            oldItem: MainScreenShimmerItem,
            newItem: MainScreenShimmerItem
        ) = oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(
            oldItem: MainScreenShimmerItem,
            newItem: MainScreenShimmerItem
        ) = oldItem.toString() == newItem.toString()
    }

}

class ItemAllBooksShimmerViewHolder(
    binding: ItemAllBooksShimmerBinding,
) : BaseViewHolder<ItemAllBooksShimmerBinding, MainScreenShimmerItem>(binding) {

    override fun onBind(item: MainScreenShimmerItem) {
        binding.root.startShimmer()
    }
}
