package com.example.bookloverfinalapp.app.ui.general_screens.screen_edit_book

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.showRoundedImage
import com.example.bookloverfinalapp.databinding.FragmentEditBookBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentEditBook :
    BaseFragment<FragmentEditBookBinding, FragmentEditBookViewModel>(FragmentEditBookBinding::inflate) {

    override val viewModel: FragmentEditBookViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() = with(binding()) {
        toolbarBlock.title.setText(R.string.edit_book)
        requireContext().showRoundedImage(
            roundedSize = 12,
            R.drawable.genre_background_first,
            poster
        )
        toolbarBlock.sortOptions.hide()
    }
}