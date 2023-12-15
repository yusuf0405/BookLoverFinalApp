package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AddNewBooksItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemMainAddBookBinding
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint

class AddNewBooksFingerprint :
    ItemFingerprint<ItemMainAddBookBinding, AddNewBooksItem> {

    override fun isRelativeItem(item: Item) = item is AddNewBooksItem

    override fun getLayoutId() = R.layout.item_main_add_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemMainAddBookBinding, AddNewBooksItem> {
        val binding = ItemMainAddBookBinding.inflate(layoutInflater, parent, false)
        return AddNewBooksViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil =
        object : DiffUtil.ItemCallback<AddNewBooksItem>() {

            override fun areItemsTheSame(
                oldItem: AddNewBooksItem,
                newItem: AddNewBooksItem
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AddNewBooksItem,
                newItem: AddNewBooksItem
            ) = oldItem == newItem
        }

}

class AddNewBooksViewHolder(
    binding: ItemMainAddBookBinding,
) : BaseViewHolder<ItemMainAddBookBinding, AddNewBooksItem>(binding) {

    override fun onBind(item: AddNewBooksItem) {
        super.onBind(item)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        startButton.setOnDownEffectClickListener {
            item.listener.onClickAddNewBook()
        }
    }
}

