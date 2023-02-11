package com.example.bookloverfinalapp.app.utils.extensions

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.adapter.snap.OnSnapPositionChangeListener
import com.example.bookloverfinalapp.app.ui.adapter.snap.SnapOnScrollListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter

fun RecyclerView.attachSnapHelperWithListener(
    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
    onSnapPositionChangeListener: OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener =
        SnapOnScrollListener(snapHelper, onSnapPositionChangeListener, behavior)
    addOnScrollListener(snapOnScrollListener)
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.setupLayoutManager(
    adapter: FingerprintAdapter,
    @LayoutRes itemId: Int = R.layout.item_book_genre
) {
    if (layoutManager !is GridLayoutManager) return
    val gridLayoutManager = layoutManager as? GridLayoutManager
    gridLayoutManager?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(viewType: Int) =
            when (adapter.getItemViewType(viewType)) {
                itemId -> 1
                else -> (layoutManager as GridLayoutManager).spanCount
            }
    }
}