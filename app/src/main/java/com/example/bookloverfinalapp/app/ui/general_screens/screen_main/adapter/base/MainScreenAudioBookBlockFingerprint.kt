package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenAudioBookBlockBinding
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint

class MainScreenAudioBookBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
) : ItemFingerprint<MainScreenAudioBookBlockBinding, MainScreenAudioBookBlockItem> {

    override fun isRelativeItem(item: Item) = item is MainScreenAudioBookBlockItem

    override fun getLayoutId() = R.layout.main_screen_audio_book_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenAudioBookBlockBinding, MainScreenAudioBookBlockItem> {
        val binding = MainScreenAudioBookBlockBinding.inflate(layoutInflater)
        return MainScreenAudioBookBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MainScreenAudioBookBlockItem>() {

        override fun areItemsTheSame(
            oldItem: MainScreenAudioBookBlockItem,
            newItem: MainScreenAudioBookBlockItem
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MainScreenAudioBookBlockItem,
            newItem: MainScreenAudioBookBlockItem
        ) =
            oldItem == newItem

    }

}

class MainScreenAudioBookBlockViewHolder(
    binding: MainScreenAudioBookBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenAudioBookBlockBinding, MainScreenAudioBookBlockItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: MainScreenAudioBookBlockItem) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        horizontalRecyclerView.startSlideInLeftAnim()
        fingerprintAdapter.submitList(item.items)
        horizontalRecyclerView.restoreState(item.state)
    }

    override fun onViewDetached() {
        binding.horizontalRecyclerView.onFlingListener = null
        item.state = binding.horizontalRecyclerView.layoutManager?.onSaveInstanceState() ?: return
    }

    private fun RecyclerView.restoreState(parcelable: Parcelable?) {
        if (parcelable == null) return
        layoutManager?.onRestoreInstanceState(parcelable)
    }

}