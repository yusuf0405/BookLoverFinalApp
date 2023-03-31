package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showRoundedImage
import com.example.bookloverfinalapp.databinding.ItemHorizontalSavedBookBinding

class SavedBookMainFingerprint :
    ItemFingerprint<ItemHorizontalSavedBookBinding, SavedBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is SavedBookAdapterModel

    override fun getLayoutId() = R.layout.item_horizontal_saved_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemHorizontalSavedBookBinding, SavedBookAdapterModel> {
        val binding = ItemHorizontalSavedBookBinding.inflate(layoutInflater, parent, false)
        return SavedBookMainViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<SavedBookAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: SavedBookAdapterModel,
            newItem: SavedBookAdapterModel
        ) = oldItem.id() == newItem.id()

        override fun areContentsTheSame(
            oldItem: SavedBookAdapterModel,
            newItem: SavedBookAdapterModel
        ) = oldItem == newItem
    }

}

class SavedBookMainViewHolder(
    binding: ItemHorizontalSavedBookBinding,
) : BaseViewHolder<ItemHorizontalSavedBookBinding, SavedBookAdapterModel>(binding) {


    override fun onBind(item: SavedBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setupProgressBar()
        setupBookPoster()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        bookDescription.text = item.savedBook.author
        bookTitle.text = item.savedBook.title
    }

    private fun setupProgressBar() = with(binding.bookProgress) {
        setTotal(item.savedBook.page - 1)
        setProgress(item.savedBook.progress)
        setProgressColor(item.getProgressColor(binding.bookDescription.context))
    }

    private fun setupBookPoster() {
        itemView.context.showRoundedImage(
            roundedSize = 8,
            imageUrl = item.savedBook.poster.url,
            imageView = binding.bookPoster
        )
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listeners.saveBookItemOnClick(item.savedBook) }
        button.setOnDownEffectClickListener { item.listeners.saveBookItemOnClick(item.savedBook) }
        removeButton.setOnDownEffectClickListener { item.listeners.saveBookItemOnLongClick(item.savedBook.bookId) }
        root.downEffect().setOnLongClickListener {
            item.listeners.saveBookItemOnLongClick(item.savedBook.bookId)
            true
        }
    }
}
