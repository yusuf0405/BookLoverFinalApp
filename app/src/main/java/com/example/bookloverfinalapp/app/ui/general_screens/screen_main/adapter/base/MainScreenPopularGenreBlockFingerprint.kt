package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.PopularGenreBlockItem
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.MainScreenPopularGenreBlockBinding
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.joseph.utils_core.extensions.toDp

class MainScreenPopularGenreBlockFingerprint(
    private val fingerprintsList: List<ItemFingerprint<*, *>>,
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool(),
) : ItemFingerprint<MainScreenPopularGenreBlockBinding, PopularGenreBlockItem> {

    override fun isRelativeItem(item: Item) = item is PopularGenreBlockItem

    override fun getLayoutId() = R.layout.main_screen_popular_genre_block

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<MainScreenPopularGenreBlockBinding, PopularGenreBlockItem> {
        val binding = MainScreenPopularGenreBlockBinding.inflate(layoutInflater)
        val newLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            400.toDp
        )
        newLayoutParams.setMargins(8.toDp, 20.toDp, 8.toDp, 20.toDp)
        binding.root.layoutParams = newLayoutParams
        return MainScreenPopularGenreBlockViewHolder(binding, fingerprintsList, viewPool)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<PopularGenreBlockItem>() {

        override fun areItemsTheSame(
            oldItem: PopularGenreBlockItem,
            newItem: PopularGenreBlockItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PopularGenreBlockItem,
            newItem: PopularGenreBlockItem
        ) = oldItem == newItem
    }

}

class MainScreenPopularGenreBlockViewHolder(
    binding: MainScreenPopularGenreBlockBinding,
    fingerprints: List<ItemFingerprint<*, *>>,
    viewPool: RecyclerView.RecycledViewPool,
) : BaseViewHolder<MainScreenPopularGenreBlockBinding, PopularGenreBlockItem>(binding) {

    private val fingerprintAdapter = FingerprintAdapter(fingerprints)

    init {
        with(binding.recyclerView) {
            adapter = fingerprintAdapter
            setRecycledViewPool(viewPool)
        }
    }

    override fun onBind(item: PopularGenreBlockItem) {
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