package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.app.models.SavedStatus
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.BookAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.ItemBookBinding
import com.joseph.ui.core.extensions.toDp
import com.joseph.core.extensions.showRoundedImage
import com.example.bookloverfinalapp.R


class BookFingerprint : ItemFingerprint<ItemBookBinding, BookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is BookAdapterModel

    override fun getLayoutId() = R.layout.item_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBookBinding, BookAdapterModel> {
        val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
        return BookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<BookAdapterModel>() {

        override fun areItemsTheSame(oldItem: BookAdapterModel, newItem: BookAdapterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookAdapterModel, newItem: BookAdapterModel) =
            oldItem == newItem
    }

}

class BookViewHolder(
    binding: ItemBookBinding,
) : BaseViewHolder<ItemBookBinding, BookAdapterModel>(binding) {

    override fun onBind(item: BookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        containerCardView.startSlideInLeftAnim()
        itemView.context.showRoundedImage(
            roundedSize= 4.toDp,
            imageUrl = item.posterUrl,
            imageView = roundedBookImage
        )
        bookAuthor.text = item.author
        publishedYear.text = item.publishYear
        bookPages.text = item.pageCount.toString()
        bookTitle.text = item.title
        when (item.savedStatus) {
            SavedStatus.SAVED -> downloadedMark.show()
            SavedStatus.NOT_SAVED -> downloadedMark.hide()
            SavedStatus.SAVING -> Unit
        }
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener { item.listener.bookItemOnClick(item.id) }
        moreButton.setOnDownEffectClickListener {
            item.listener.bookOptionMenuOnClick(bookId = item.id)
        }
    }
}

