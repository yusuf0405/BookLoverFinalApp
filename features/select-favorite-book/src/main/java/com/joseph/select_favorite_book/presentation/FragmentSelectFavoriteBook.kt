package com.joseph.select_favorite_book.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.joseph.common.base.BaseBindingFragment
import com.joseph.select_favorite_book.databinding.FragmentSelectFavoriteBookBinding
import com.joseph.select_favorite_book.presentation.adapter.SelectFavoriteBookFingerprint
import com.joseph.ui.core.MyApplicationTheme
import androidx.compose.material.Surface
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.managers.PeekingLinearLayoutManager
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.attachSnapHelperWithListener
import com.joseph.core.extensions.showBlurImage
import com.joseph.core.snap.OnSnapPositionChangeListener
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        requireActivity()
        setContent {
            MyApplicationTheme(
                darkTheme = true
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    SelectFavoriteBookScreen(
                        uiState = viewModel.uiState
                    )
                }

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupViews()
//        observeResource()
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