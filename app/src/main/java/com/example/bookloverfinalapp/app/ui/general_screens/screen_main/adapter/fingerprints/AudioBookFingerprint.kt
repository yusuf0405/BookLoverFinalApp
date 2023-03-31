package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showRoundedImage
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
        val context = itemView.context
        container.startSlideInLeftAnim()
        context.showRoundedImage(8, item.audioBook.audioBookPoster.url, cover)
        title.text = item.audioBook.title
        author.text = item.audioBook.author
        val actualIcon = if (item.audioBook.isPlaying) R.drawable.pause_icon
        else R.drawable.play_icon
        playOrPauseIcon.setImageDrawable(ContextCompat.getDrawable(context, actualIcon))
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.audioBookItemOnClick(item.audioBook.id)
        }
    }

}

