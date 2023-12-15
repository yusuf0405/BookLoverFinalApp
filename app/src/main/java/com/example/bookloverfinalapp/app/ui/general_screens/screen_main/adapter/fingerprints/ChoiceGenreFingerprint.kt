package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.GenreAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemChoiceGenreBinding
import com.example.bookloverfinalapp.R


class ChoiceGenreFingerprint : ItemFingerprint<ItemChoiceGenreBinding, GenreAdapterModel> {

    override fun isRelativeItem(item: Item) = item is GenreAdapterModel

    override fun getLayoutId() = R.layout.item_choice_genre

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemChoiceGenreBinding, GenreAdapterModel> {
        val binding = ItemChoiceGenreBinding.inflate(layoutInflater, parent, false)
        return ChoiceGenreHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<GenreAdapterModel>() {

        override fun areItemsTheSame(oldItem: GenreAdapterModel, newItem: GenreAdapterModel) =
            true

        override fun areContentsTheSame(oldItem: GenreAdapterModel, newItem: GenreAdapterModel) =
            true
    }

}

class ChoiceGenreHolder(
    binding: ItemChoiceGenreBinding,
) : BaseViewHolder<ItemChoiceGenreBinding, GenreAdapterModel>(binding) {

    override fun onBind(item: GenreAdapterModel) {
        super.onBind(item)
        with(binding) {
            container.startSlideInLeftAnim()
            genreName.text = item.genreTitle
            genreBooksSize.text = item.genreBooksSize.toString()
            root.downEffect().setOnClickListener {
                item.listeners.genreItemOnClickListeners(item.genreCode)
            }
        }
    }
}

