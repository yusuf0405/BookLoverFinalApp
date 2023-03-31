package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.GenreBlockAdapterItem
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.MainScreenGenreBlockBinding
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint

class MainScreenGenreBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
) : ItemFingerprint<MainScreenGenreBlockBinding, GenreBlockAdapterItem> {

    override fun isRelativeItem(item: Item) = item is GenreBlockAdapterItem

    override fun getLayoutId() = R.layout.main_screen_genre_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenGenreBlockBinding, GenreBlockAdapterItem> {
        val binding = MainScreenGenreBlockBinding.inflate(layoutInflater)
        return MainScreenGenreBlockViewHolder(
            binding,
            fingerprintsList,
        )
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<GenreBlockAdapterItem>() {

        override fun areItemsTheSame(
            oldItem: GenreBlockAdapterItem,
            newItem: GenreBlockAdapterItem
        ) =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: GenreBlockAdapterItem,
            newItem: GenreBlockAdapterItem
        ) = oldItem == newItem

    }

}

class MainScreenGenreBlockViewHolder(
    binding: MainScreenGenreBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
) : BaseViewHolder<MainScreenGenreBlockBinding, GenreBlockAdapterItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.horizontalRecyclerView) {
            adapter = fingerprintAdapter
        }
    }

    override fun onBind(item: GenreBlockAdapterItem) {
        super.onBind(item)
        val recyclerView = binding.horizontalRecyclerView
        fingerprintAdapter.submitList(item.items)
        binding.showAllButton.setOnDownEffectClickListener {
            item.listeners.genreBlockButtonOnClickListener()
        }
        recyclerView.restoreState(item.state)
    }

    override fun onViewDetached() {
//        binding.horizontalRecyclerView.onFlingListener = null
//        item.state = binding.horizontalRecyclerView.layoutManager?.onSaveInstanceState() ?: return
    }

    private fun RecyclerView.restoreState(parcelable: Parcelable?) {
//        if (parcelable == null) return
//        layoutManager?.onRestoreInstanceState(parcelable)
    }
}