package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenCollectionsBlockBinding
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.ui.core.extensions.toDp
import com.example.bookloverfinalapp.R

class MainScreenCollectionsBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
) : ItemFingerprint<MainScreenCollectionsBlockBinding, MainScreenCollectionsBlockItem> {

    override fun isRelativeItem(item: Item) = item is MainScreenCollectionsBlockItem

    override fun getLayoutId() = R.layout.main_screen_collections_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenCollectionsBlockBinding, MainScreenCollectionsBlockItem> {
        val binding = MainScreenCollectionsBlockBinding.inflate(layoutInflater)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newLayoutParams.setMargins(4.toDp, 8.toDp, 8.toDp, 16.toDp)
        binding.root.layoutParams = newLayoutParams
        return MainScreenCollectionsBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<MainScreenCollectionsBlockItem>() {

        override fun areItemsTheSame(
            oldItem: MainScreenCollectionsBlockItem,
            newItem: MainScreenCollectionsBlockItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MainScreenCollectionsBlockItem,
            newItem: MainScreenCollectionsBlockItem
        ) = oldItem == newItem

    }

}

class MainScreenCollectionsBlockViewHolder(
    binding: MainScreenCollectionsBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenCollectionsBlockBinding,
        MainScreenCollectionsBlockItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.recyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: MainScreenCollectionsBlockItem) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        fingerprintAdapter.submitList(item.items)
        recyclerView.apply {
            startSlideInLeftAnim()
            restoreState(item.state)
        }
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