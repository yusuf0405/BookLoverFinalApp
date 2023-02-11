package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.BookGenreAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showImage
import com.example.bookloverfinalapp.app.utils.extensions.showRoundedImage
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.app.utils.genre.checkLanguageAndGetActualString
import com.example.bookloverfinalapp.databinding.ItemAudioBookBinding
import com.example.bookloverfinalapp.databinding.ItemBookBinding
import com.example.bookloverfinalapp.databinding.ItemBookGenreBinding


class BookGenreFingerprint : ItemFingerprint<ItemBookGenreBinding, BookGenreAdapterModel> {

    override fun isRelativeItem(item: Item) = item is BookGenreAdapterModel

    override fun getLayoutId() = R.layout.item_book_genre

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemBookGenreBinding, BookGenreAdapterModel> {
        val binding = ItemBookGenreBinding.inflate(layoutInflater, parent, false)
        return BookGenreViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<BookGenreAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: BookGenreAdapterModel,
            newItem: BookGenreAdapterModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: BookGenreAdapterModel,
            newItem: BookGenreAdapterModel
        ) = oldItem == newItem
    }
}

class BookGenreViewHolder(
    binding: ItemBookGenreBinding,
) : BaseViewHolder<ItemBookGenreBinding, BookGenreAdapterModel>(binding) {

    override fun onBind(item: BookGenreAdapterModel) {
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

