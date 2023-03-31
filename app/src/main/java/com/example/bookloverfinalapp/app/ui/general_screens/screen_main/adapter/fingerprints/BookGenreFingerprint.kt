package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.BookGenreItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.app.utils.genre.checkLanguageAndGetActualString
import com.example.bookloverfinalapp.databinding.ItemBookGenreBinding


class BookGenreFingerprint : ItemFingerprint<ItemBookGenreBinding, BookGenreItem> {

    override fun isRelativeItem(item: Item) = item is BookGenreItem

    override fun getLayoutId() = R.layout.item_book_genre

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBookGenreBinding, BookGenreItem> {
        val binding = ItemBookGenreBinding.inflate(layoutInflater, parent, false)
        return BookGenreViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<BookGenreItem>() {

        override fun areItemsTheSame(
            oldItem: BookGenreItem,
            newItem: BookGenreItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: BookGenreItem,
            newItem: BookGenreItem
        ) = oldItem == newItem
    }
}

class BookGenreViewHolder(
    binding: ItemBookGenreBinding,
) : BaseViewHolder<ItemBookGenreBinding, BookGenreItem>(binding) {

    override fun onBind(item: BookGenreItem) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        container.startSlideInLeftAnim()
        itemView.context.showImage(item.posterUrl, cover)
        title.text = checkLanguageAndGetActualString(item.titles)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.bookGenreOnClickListener(item.id)
        }
    }

}

