package com.example.bookloverfinalapp.app.ui.student_screens.screen_reader

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Progress
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.github.barteksc.pdfviewer.listener.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FragmentReader :
    BaseFragment<FragmentReaderBinding, FragmentReaderViewModel>(FragmentReaderBinding::inflate),
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener, OnDrawListener,
    View.OnClickListener, OnRenderListener, OnTapListener {
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
        binding().pdfview.fromFile(File(book.book))
            .spacing(100)
            .pages(*pages.toIntArray())
            .onDraw(this)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .onPageChange(this)
            .onLoad(this)
            .onRender(this)
            .enableAnnotationRendering(true)
            .password(null)
            .onTap(this)
            .onRender(this)
            .scrollHandle(null)
            .load()
        binding().pdfview.zoom
    }


    private fun setOnClickListeners() {
        binding().apply {
            endRead.setOnClickListener(this@FragmentReader)
            saveChanges.setOnClickListener(this@FragmentReader)
            pageLast.setOnClickListener(this@FragmentReader)
            pageFirst.setOnClickListener(this@FragmentReader)
            pageForward.setOnClickListener(this@FragmentReader)
            pageBack.setOnClickListener(this@FragmentReader)
        }
    }

    override fun loadComplete(nbPages: Int) {
    }

    override fun onError(t: Throwable?) {
        showToast(message = "Error!!")
        viewModel.goBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        binding().apply {
            pageEnterField.text = pages[page].toString()
            if (page == pageCount - 1) {
                saveChanges.hideView()
                endRead.showView()
            } else {
                saveChanges.showView()
                endRead.hideView()
            }
            progress = pages[page]
        }

    }

    override fun onLayerDrawn(
        canvas: Canvas?,
        pageWidth: Float,
        pageHeight: Float,
        displayedPage: Int,
    ) {
    }

    private fun saveChanges(type: Boolean) {
        if (bookCurrentProgress < progress + 1) {
            viewModel.updateProgress(id = book.objectId,
                Progress(progress = progress),
                type = type,
                book = book,
                chapter = chapter)
            bookCurrentProgress = progress
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().endRead -> saveChanges(type = true)
            binding().saveChanges -> saveChanges(type = false)
            binding().pageBack -> binding().pdfview.jumpTo(binding().pdfview.currentPage - 1)
            binding().pageForward -> binding().pdfview.jumpTo(binding().pdfview.currentPage + 1)
            binding().pageFirst -> binding().pdfview.jumpTo(0)
            binding().pageLast -> binding().pdfview.jumpTo(pages.size)

        }
    }

    override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
        Log.i("pageHeight", pageHeight.toString())
        Log.i("pageWidth", pageWidth.toString())
        binding().pdfview.fitToWidth()
        binding().pdfview.moveTo(pageHeight, pageWidth)

    }

    override fun onTap(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}




