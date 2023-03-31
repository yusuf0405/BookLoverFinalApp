package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenExclusiveAudioBookBlockBinding
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.joseph.ui_core.adapter.managers.PeekingLinearLayoutManager
import com.joseph.utils_core.extensions.attachSnapHelperWithListener

class MainScreenExclusiveAudioBookBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
) : ItemFingerprint<MainScreenExclusiveAudioBookBlockBinding, ExclusiveAudioBookHorizontalItem> {

    override fun isRelativeItem(item: Item) = item is ExclusiveAudioBookHorizontalItem

    override fun getLayoutId() = R.layout.main_screen_exclusive_audio_book_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenExclusiveAudioBookBlockBinding, ExclusiveAudioBookHorizontalItem> {
        val binding = MainScreenExclusiveAudioBookBlockBinding.inflate(layoutInflater)
        return MainScreenExclusiveAudioBookBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ExclusiveAudioBookHorizontalItem>() {

        override fun areItemsTheSame(
            oldItem: ExclusiveAudioBookHorizontalItem,
            newItem: ExclusiveAudioBookHorizontalItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ExclusiveAudioBookHorizontalItem,
            newItem: ExclusiveAudioBookHorizontalItem
        ) = oldItem == newItem

    }

}

class MainScreenExclusiveAudioBookBlockViewHolder(
    binding: MainScreenExclusiveAudioBookBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenExclusiveAudioBookBlockBinding,
        ExclusiveAudioBookHorizontalItem>(binding),
    com.joseph.utils_core.snap.OnSnapPositionChangeListener {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            layoutManager = PeekingLinearLayoutManager(itemView.context)
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: ExclusiveAudioBookHorizontalItem) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        fingerprintAdapter.submitList(item.items)
        horizontalRecyclerView.apply {
            startSlideInLeftAnim()
            restoreState(item.state)
            if (horizontalRecyclerView.onFlingListener != null) return@with
            attachSnapHelperWithListener(
                snapHelper = PagerSnapHelper(),
                onSnapPositionChangeListener = this@MainScreenExclusiveAudioBookBlockViewHolder
            )
        }
    }

    override fun onViewDetached() {
        binding.horizontalRecyclerView.onFlingListener = null
        item.state = binding.horizontalRecyclerView.layoutManager?.onSaveInstanceState() ?: return
    }

    private fun RecyclerView.restoreState(parcelable: Parcelable?) {
        if (parcelable == null) return
        layoutManager?.onRestoreInstanceState(parcelable)
    }

    override fun onSnapPositionChange(position: Int) {
        Log.i("Josephhh","position = $position")
    }

}