package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenBookBlockBinding

class MainScreenBookBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool,
) : ItemFingerprint<MainScreenBookBlockBinding, BookHorizontalItem> {

    override fun isRelativeItem(item: Item) = item is BookHorizontalItem

    override fun getLayoutId() = R.layout.main_screen_book_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenBookBlockBinding, BookHorizontalItem> {
        val binding = MainScreenBookBlockBinding.inflate(layoutInflater)
        return MainScreenBookBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<BookHorizontalItem>() {

        override fun areItemsTheSame(
            oldItem: BookHorizontalItem,
            newItem: BookHorizontalItem
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: BookHorizontalItem,
            newItem: BookHorizontalItem
        ) =
            oldItem == newItem

    }

}

class MainScreenBookBlockViewHolder(
    binding: MainScreenBookBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenBookBlockBinding, BookHorizontalItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: BookHorizontalItem) {
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