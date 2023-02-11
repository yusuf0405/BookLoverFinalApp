package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showRoundedImage
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemAudioBookBinding


class AudioBookFingerprint : ItemFingerprint<ItemAudioBookBinding, AudioBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is AudioBookAdapterModel

    override fun getLayoutId() = R.layout.item_audio_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemAudioBookBinding, AudioBookAdapterModel> {
        val binding = ItemAudioBookBinding.inflate(layoutInflater, parent, false)
        return AudioBookViewHolder(binding)
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

class AudioBookViewHolder(
    binding: ItemAudioBookBinding,
) : BaseViewHolder<ItemAudioBookBinding, AudioBookAdapterModel>(binding) {

    override fun onBind(item: AudioBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        container.startSlideInLeftAnim()
        cover.context.showRoundedImage(8, item.audioBook.audioBookPoster.url, cover)
        title.text = item.audioBook.title
        author.text = item.audioBook.author
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.audioBookItemOnClick(item.audioBook.id)
        }
    }

}

