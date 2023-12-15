package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.PosterItem
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online.fingerprint.PosterFingerprint
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.databinding.FragmentChoosePosterOnlineBinding
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import com.joseph.ui.core.extensions.launchOnViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class FragmentChoosePosterOnline :
    BaseBindingFragment<FragmentChoosePosterOnlineBinding>(FragmentChoosePosterOnlineBinding::inflate) {

    private val viewModel: FragmentChoosePosterOnlineViewModel by viewModels()

    private var onPosterSelectedListener: ((String) -> Unit)? = null

    private val adapter = FingerprintAdapter(
        listOf(PosterFingerprint())
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        recyclerView.adapter = adapter
    }


    private fun observeResource() = with(viewModel) {
        launchOnViewLifecycle {
            postersUrlFlow
                .map(::mapPostersToPosterItem)
                .observe(adapter::submitList)
        }
    }

    private fun mapPostersToPosterItem(posters: List<String>) = posters.map {
        PosterItem(
            posterUrl = it,
            onClickListener = ::handlePosterOnClickListener
        )
    }

    private fun handlePosterOnClickListener(posterUrl: String) {
        onPosterSelectedListener?.invoke(posterUrl)
        dismissModalPage()
    }

    companion object {

        fun newInstance(
            onPosterSelectedListener: (String) -> Unit
        ) = FragmentChoosePosterOnline().apply {
            this.onPosterSelectedListener = onPosterSelectedListener
        }
    }
}