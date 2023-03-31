package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.UserBlockAdapterItem
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenUserBlockBinding
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint

class MainScreenUserBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool,
) : ItemFingerprint<MainScreenUserBlockBinding, UserBlockAdapterItem> {

    override fun isRelativeItem(item: Item) = item is UserBlockAdapterItem

    override fun getLayoutId() = R.layout.main_screen_user_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenUserBlockBinding, UserBlockAdapterItem> {
        val binding = MainScreenUserBlockBinding.inflate(layoutInflater)
        return HorizontalGridItemsHolder(
            binding,
            fingerprintsList,
            viewPool,
        )
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<UserBlockAdapterItem>() {

        override fun areItemsTheSame(
            oldItem: UserBlockAdapterItem,
            newItem: UserBlockAdapterItem
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: UserBlockAdapterItem,
            newItem: UserBlockAdapterItem
        ) =
            oldItem == newItem

    }

}

class HorizontalGridItemsHolder(
    binding: MainScreenUserBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenUserBlockBinding, UserBlockAdapterItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: UserBlockAdapterItem) {
        super.onBind(item)
        binding.container.startSlideInLeftAnim()
        val recyclerView = binding.horizontalRecyclerView
        fingerprintAdapter.submitList(item.items)
        recyclerView.restoreState(item.state)
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
