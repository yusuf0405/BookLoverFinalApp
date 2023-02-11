package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.adapter.snap.OnSnapPositionChangeListener
import com.example.bookloverfinalapp.app.utils.extensions.attachSnapHelperWithListener
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemSavedListBinding
import com.example.bookloverfinalapp.databinding.ItemSavedListSecondBinding

class HorizontalItemsFingerprintSecond(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool,
) : ItemFingerprint<ItemSavedListSecondBinding, HorizontalItemSecond> {

    override fun isRelativeItem(item: Item) = item is HorizontalItemSecond

    override fun getLayoutId() = R.layout.item_saved_list_second

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSavedListSecondBinding, HorizontalItemSecond> {
        val binding = ItemSavedListSecondBinding.inflate(layoutInflater)
        return HorizontalItemsHolderSecond(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalItemSecond>() {

        override fun areItemsTheSame(oldItem: HorizontalItemSecond, newItem: HorizontalItemSecond) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: HorizontalItemSecond,
            newItem: HorizontalItemSecond
        ) =
            oldItem == newItem

    }

}

class HorizontalItemsHolderSecond(
    binding: ItemSavedListSecondBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<ItemSavedListSecondBinding, HorizontalItemSecond>(binding),
    OnSnapPositionChangeListener {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: HorizontalItemSecond) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        container.startSlideInLeftAnim()
        fingerprintAdapter.submitList(item.items)
        horizontalRecyclerView.restoreState(item.state)
        horizontalRecyclerView.attachSnapHelperWithListener(
            snapHelper = PagerSnapHelper(),
            onSnapPositionChangeListener = this@HorizontalItemsHolderSecond,
        )
    }

    override fun onViewDetached() {
        binding.horizontalRecyclerView.onFlingListener = null
        item.state = binding.horizontalRecyclerView.layoutManager?.onSaveInstanceState() ?: return
    }

    private fun RecyclerView.restoreState(parcelable: Parcelable?) {
        if (parcelable == null) return
        layoutManager?.onRestoreInstanceState(parcelable)
    }

    override fun onSnapPositionChange(position: Int) = Unit
}