package com.example.bookloverfinalapp.app.ui.screen_progress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FragmentProgress() :
    BaseFragment<FragmentProgressBinding, FragmentProgressViewModel>(
        FragmentProgressBinding::inflate) {

    override val viewModel: FragmentProgressViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
        binding().studentProgressImg.setOnClickListener { viewModel.go() }
    }

    private fun observeResource() {
        viewModel.fetchMyBook(id = currentUser.id)
        viewModel.observe(viewLifecycleOwner) { list ->
            responseSuccess(list = list)
        }
        viewModel.observeProgressAnimation(viewLifecycleOwner) {
            binding().apply {
                it.getValue()?.let { status ->
                    if (status) {
                        progressLoadingAnimation.showView()
                        progressMaterialCardForBooks.hideView()
                        progressMaterialCardViewForChapters.hideView()
                        progressMaterialCardViewForHours.hideView()
                        progressMaterialCardViewForPages.hideView()
                    } else {
                        progressMaterialCardForBooks.showView()
                        progressMaterialCardViewForChapters.showView()
                        progressMaterialCardViewForHours.showView()
                        progressMaterialCardViewForPages.showView()
                        progressLoadingAnimation.hideView()
                    }

                }
            }
        }
    }

    private fun responseSuccess(list: List<BookThatRead>) {
        var readPages = 0
        var readChapters = 0
        var readBooks = 0
        list.forEach { books ->
            readChapters += books.chaptersRead
            readPages += books.progress
            if (books.isReadingPages[books.isReadingPages.lastIndex]) readBooks += 1
        }
        binding().apply {
            progressDiamondReadText.text = readChapters.toString()
            progressBookReadText.text = readBooks.toString()
            progressPageReadText.text = readPages.toString()
            progressMaterialCardForBooks.showView()
            progressMaterialCardViewForChapters.showView()
            progressMaterialCardViewForHours.showView()
            progressMaterialCardViewForPages.showView()
        }
    }

    private fun setupUi() {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        binding().apply {
            currentUser.apply {
                val fullName = "$name $lastname"
                progressProfileName.text = fullName
                val readTime = formatter.format(createAt).toString()
                progressReadTime.text = readTime
                Glide.with(requireActivity())
                    .load(image?.url)
                    .placeholder(R.drawable.placeholder_avatar)
                    .into(studentProgressImg)
            }
        }
    }
}