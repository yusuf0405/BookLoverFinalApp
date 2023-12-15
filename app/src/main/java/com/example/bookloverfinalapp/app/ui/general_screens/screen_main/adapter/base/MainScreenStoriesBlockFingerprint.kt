package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenStoriesBlockBinding
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.ui.core.extensions.toDp
import com.example.bookloverfinalapp.R

class MainScreenStoriesBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
) : ItemFingerprint<MainScreenStoriesBlockBinding, MainScreenStoriesBlockItem> {

    override fun isRelativeItem(item: Item) = item is MainScreenStoriesBlockItem

    override fun getLayoutId() = R.layout.main_screen_stories_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenStoriesBlockBinding, MainScreenStoriesBlockItem> {
        val binding = MainScreenStoriesBlockBinding.inflate(layoutInflater)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newLayoutParams.setMargins(0.toDp, 0.toDp, 8.toDp, 16.toDp)
        binding.root.layoutParams = newLayoutParams
        return MainScreenStoriesBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MainScreenStoriesBlockItem>() {

        override fun areItemsTheSame(
            oldItem: MainScreenStoriesBlockItem,
            newItem: MainScreenStoriesBlockItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MainScreenStoriesBlockItem,
            newItem: MainScreenStoriesBlockItem
        ) = oldItem == newItem

    }

}

class MainScreenStoriesBlockViewHolder(
    binding: MainScreenStoriesBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenStoriesBlockBinding, MainScreenStoriesBlockItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.recyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: MainScreenStoriesBlockItem) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        recyclerView.startSlideInLeftAnim()
        fingerprintAdapter.submitList(item.items)
        recyclerView.restoreState(item.state)
    }

    override fun onViewDetached() {
        binding.recyclerView.onFlingListener = null
        item.state = binding.recyclerView.layoutManager?.onSaveInstanceState() ?: return
    }

    private fun RecyclerView.restoreState(parcelable: Parcelable?) {
        if (parcelable == null) return
        layoutManager?.onRestoreInstanceState(parcelable)
    }

}