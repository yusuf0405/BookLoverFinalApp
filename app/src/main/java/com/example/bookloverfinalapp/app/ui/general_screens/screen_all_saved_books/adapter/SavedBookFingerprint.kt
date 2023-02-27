package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.app.utils.extensions.showRoundedImage
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemSavedBookBinding
import com.joseph.ui_core.extensions.toDp

class SavedBookFingerprint : ItemFingerprint<ItemSavedBookBinding, SavedBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is SavedBookAdapterModel

    override fun getLayoutId() = R.layout.item_saved_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSavedBookBinding, SavedBookAdapterModel> {
        val binding = ItemSavedBookBinding.inflate(layoutInflater, parent, false)
        return SavedBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<SavedBookAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: SavedBookAdapterModel,
            newItem: SavedBookAdapterModel
        ) = oldItem.savedBook.bookId == newItem.savedBook.bookId

        override fun areContentsTheSame(
            oldItem: SavedBookAdapterModel,
            newItem: SavedBookAdapterModel
        ) = oldItem.savedBook == newItem.savedBook
    }

}

class SavedBookViewHolder(
    binding: ItemSavedBookBinding,
) : BaseViewHolder<ItemSavedBookBinding, SavedBookAdapterModel>(binding) {

    override fun onBind(item: SavedBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setupProgressBar()
        setupBookPoster()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        container.startSlideInLeftAnim()
        val savedBook = item.savedBook
        bookAuthor.text = savedBook.author
        bookTitle.text = savedBook.title
        bookPages.text = savedBook.page.toString()
        countOfDimonds.text = savedBook.chaptersRead.toString()
        countOfPages.text = savedBook.progress.toString()
        bookCratedAtText.text = item.getCreatedAtText(bookCratedAtText.context)
        publishedYear.text = savedBook.publicYear
    }

    private fun setupProgressBar() = with(binding.bookProgress) {
        val savedBook = item.savedBook
        setTotal(savedBook.page - 1)
        setProgress(item.savedBook.progress)
        setProgressColor(item.getProgressColor(binding.bookAuthor.context))
    }

    private fun setupBookPoster() = itemView.context.showRoundedImage(
        roundedSize = 4.toDp,
        imageUrl = item.savedBook.poster.url,
        imageView = binding.roundedBookImage
    )

    private fun setOnClickListeners() = with(binding) {
        root.downEffect().setOnClickListener { item.listeners.saveBookItemOnClick(item.savedBook) }
        root.downEffect().setOnLongClickListener {
            item.listeners.saveBookItemOnLongClick(item.savedBook.bookId)
            true
        }
    }
}