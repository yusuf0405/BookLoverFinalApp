package com.joseph.select_favorite_book.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.joseph.common_api.base.BaseBindingFragment
import com.joseph.select_favorite_book.databinding.FragmentSelectFavoriteBookBinding
import com.joseph.select_favorite_book.presentation.adapter.SelectFavoriteBookFingerprint
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.managers.PeekingLinearLayoutManager
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.attachSnapHelperWithListener
import com.joseph.utils_core.extensions.showBlurImage
import com.joseph.utils_core.snap.OnSnapPositionChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class FragmentSelectFavoriteBook :
    BaseBindingFragment<FragmentSelectFavoriteBookBinding>(FragmentSelectFavoriteBookBinding::inflate),
    OnSnapPositionChangeListener {

    private val viewModel by viewModels<FragmentSelectFavoriteBookViewModel>()

    private val adapter = FingerprintAdapter(
        listOf(
            SelectFavoriteBookFingerprint()
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = PeekingLinearLayoutManager(requireContext())
        recyclerView.attachSnapHelperWithListener(
            snapHelper = PagerSnapHelper(),
            onSnapPositionChangeListener = this@FragmentSelectFavoriteBook
        )
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            selectBooksFlow.filter { it.isNotEmpty() }.observe(adapter::submitList)
            posterUrlFlow.observe(::setBackgroundPoster)
        }
    }

    private fun setBackgroundPoster(posterUrl: String) {
        requireContext().showBlurImage(
            blurSize = BLUR_SIZE,
            imageUrl = posterUrl,
            imageView = binding().posterBlurBackground
        )
    }

    override fun onSnapPositionChange(position: Int) {
        viewModel.setCurrentShowedItemPosition(position)
    }

    private companion object {
        const val BLUR_SIZE = 120f
    }
}