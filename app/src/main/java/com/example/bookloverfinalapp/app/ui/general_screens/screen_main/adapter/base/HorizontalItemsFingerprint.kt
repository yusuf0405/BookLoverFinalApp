package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.adapter.snap.OnSnapPositionChangeListener
import com.example.bookloverfinalapp.app.utils.extensions.attachSnapHelperWithListener
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemSavedListBinding

class HorizontalItemsFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool,
) : ItemFingerprint<ItemSavedListBinding, HorizontalItem> {

    override fun isRelativeItem(item: Item) = item is HorizontalItem

    override fun getLayoutId() = R.layout.item_saved_list

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSavedListBinding, HorizontalItem> {
        val binding = ItemSavedListBinding.inflate(layoutInflater)
        return HorizontalItemsHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalItem>() {

        override fun areItemsTheSame(oldItem: HorizontalItem, newItem: HorizontalItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: HorizontalItem, newItem: HorizontalItem) =
            oldItem == newItem

    }

}

class HorizontalItemsHolder(
    binding: ItemSavedListBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<ItemSavedListBinding, HorizontalItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: HorizontalItem) {
        super.onBind(item)
        binding.horizontalRecyclerView.startSlideInLeftAnim()
        fingerprintAdapter.submitList(item.items)
        binding.horizontalRecyclerView.restoreState(item.state)
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