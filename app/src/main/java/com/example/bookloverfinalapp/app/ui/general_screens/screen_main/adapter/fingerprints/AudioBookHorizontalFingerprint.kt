package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemSquareAudioBookBinding


class AudioBookHorizontalFingerprint :
    ItemFingerprint<ItemSquareAudioBookBinding, AudioBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is AudioBookAdapterModel

    override fun getLayoutId() = R.layout.item_square_audio_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSquareAudioBookBinding, AudioBookAdapterModel> {
        val binding = ItemSquareAudioBookBinding.inflate(layoutInflater, parent, false)
        return AudioBookHorizontalViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<AudioBookAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: AudioBookAdapterModel,
            newItem: AudioBookAdapterModel
        ) = oldItem.id() == newItem.id()

        override fun areContentsTheSame(
            oldItem: AudioBookAdapterModel,
            newItem: AudioBookAdapterModel
        ) = oldItem == newItem
    }
}

class AudioBookHorizontalViewHolder(
    binding: ItemSquareAudioBookBinding,
) : BaseViewHolder<ItemSquareAudioBookBinding, AudioBookAdapterModel>(binding) {

    override fun onBind(item: AudioBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        poster.context.showImage(item.audioBook.audioBookPoster.url, poster)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.audioBookItemOnClick(item.audioBook.id)
        }
        button.setOnDownEffectClickListener {
            item.listener.audioBookItemOnClick(item.audioBook.id)
        }
    }

}

