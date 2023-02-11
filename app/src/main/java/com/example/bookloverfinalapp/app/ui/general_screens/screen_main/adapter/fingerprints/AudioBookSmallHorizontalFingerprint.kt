package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemSquareAudioBookBinding
import com.joseph.ui_core.extensions.toDp


class AudioBookSmallHorizontalFingerprint : ItemFingerprint<ItemSquareAudioBookBinding, AudioBookAdapterModel> {

    override fun isRelativeItem(item: Item) = item is AudioBookAdapterModel

    override fun getLayoutId() = R.layout.item_square_audio_book

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSquareAudioBookBinding, AudioBookAdapterModel> {
        val binding = ItemSquareAudioBookBinding.inflate(layoutInflater, parent, false)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newLayoutParams.setMargins(10.toDp, 4.toDp, 8.toDp, 10.toDp)
        binding.root.layoutParams = newLayoutParams
        return AudioBookSmallHorizontalViewHolder(binding)
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

class AudioBookSmallHorizontalViewHolder(
    binding: ItemSquareAudioBookBinding,
) : BaseViewHolder<ItemSquareAudioBookBinding, AudioBookAdapterModel>(binding) {

    override fun onBind(item: AudioBookAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        button.hide()
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

