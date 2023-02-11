package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenExclusiveBookBlockBinding
import com.joseph.ui_core.extensions.toDp

class MainScreenExclusiveBookBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool,
) : ItemFingerprint<MainScreenExclusiveBookBlockBinding, ExclusiveBookHorizontalItem> {

    override fun isRelativeItem(item: Item) = item is ExclusiveBookHorizontalItem

    override fun getLayoutId() = R.layout.main_screen_exclusive_book_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenExclusiveBookBlockBinding, ExclusiveBookHorizontalItem> {
        val binding = MainScreenExclusiveBookBlockBinding.inflate(layoutInflater)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newLayoutParams.setMargins(0.toDp, 0.toDp, 8.toDp, 0.toDp)
        binding.root.layoutParams = newLayoutParams
        return MMainScreenExclusiveBookBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ExclusiveBookHorizontalItem>() {

        override fun areItemsTheSame(
            oldItem: ExclusiveBookHorizontalItem,
            newItem: ExclusiveBookHorizontalItem
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ExclusiveBookHorizontalItem,
            newItem: ExclusiveBookHorizontalItem
        ) =
            oldItem == newItem

    }

}

class MMainScreenExclusiveBookBlockViewHolder(
    binding: MainScreenExclusiveBookBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenExclusiveBookBlockBinding, ExclusiveBookHorizontalItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: ExclusiveBookHorizontalItem) {
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