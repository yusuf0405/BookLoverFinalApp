package com.example.bookloverfinalapp.app.ui.screen_reader

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FragmentReader :
    BaseFragment<FragmentReaderBinding, FragmentReaderViewModel>(FragmentReaderBinding::inflate),
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener, OnDrawListener,
    View.OnClickListener {
    override val viewModel: FragmentReaderViewModel by viewModels()
    override fun onReady(savedInstanceState: Bundle?) {}

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).book
    }
    private val startPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).startPage
    }
    private val lastPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).lastPage
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).chapter
    }
    private val path: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).path
    }
    private val pages = arrayListOf<Int>()
    private var progress = 0
    private var bookCurrentProgress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeResource()
    }

    private fun observeResource() {
        loadSuccess()
    }

    private fun loadSuccess() {
        bookCurrentProgress = book.progress
        for (i in startPage until lastPage) {
            pages.add(i)
        }
        binding().pdfview.fromFile(File(path))
            .spacing(100)
            .pages(*pages.toIntArray())
            .onDraw(this)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .onPageChange(this)
            .onLoad(this)
            .enableAnnotationRendering(true)
            .password(null)
            .scrollHandle(null)
            .load()
    }


    private fun setOnClickListeners() {
        binding().apply {
            endRead.setOnClickListener(this@FragmentReader)
            pageLast.setOnClickListener(this@FragmentReader)
            pageFirst.setOnClickListener(this@FragmentReader)
            pageForward.setOnClickListener(this@FragmentReader)
            pageBack.setOnClickListener(this@FragmentReader)
        }
    }

    override fun loadComplete(nbPages: Int) {}

    override fun onError(t: Throwable?) {
        showToast(R.string.generic_error)
        viewModel.goBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        binding().apply {
            pageEnterField.text = pages[page].toString()
            if (page == pageCount - 1) {
                if (bookCurrentProgress <= progress) endRead.showView()
            } else endRead.hideView()

            progress = pages[page]
        }

    }

    override fun onLayerDrawn(
        canvas: Canvas?,
        pageWidth: Float,
        pageHeight: Float,
        displayedPage: Int,
    ) {}


    private fun saveChanges() {
        if (bookCurrentProgress < progress) {
            viewModel.updateProgress(id = book.objectId, progress = progress)
            bookCurrentProgress = progress
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().endRead -> viewModel.goQuestionFragment(book = book,
                chapter = chapter,
                path = path)
            binding().pageBack -> binding().pdfview.jumpTo(binding().pdfview.currentPage - 1)
            binding().pageForward -> binding().pdfview.jumpTo(binding().pdfview.currentPage + 1)
            binding().pageFirst -> binding().pdfview.jumpTo(0)
            binding().pageLast -> binding().pdfview.jumpTo(pages.size)

        }
    }

    override fun onPause() {
        super.onPause()
        saveChanges()
    }

}




