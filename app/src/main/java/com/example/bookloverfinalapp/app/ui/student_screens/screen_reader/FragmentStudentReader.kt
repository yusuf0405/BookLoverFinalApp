package com.example.bookloverfinalapp.app.ui.student_screens.screen_reader

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.launchWhenStarted
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.example.domain.models.Status
import com.example.domain.models.book.BookUpdateProgress
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import java.io.InputStream


@AndroidEntryPoint
class FragmentStudentReader :
    BaseFragment<FragmentReaderBinding, FragmentStudentReaderViewModel>(FragmentReaderBinding::inflate),
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener, OnDrawListener,
    View.OnClickListener {
    override val viewModel: FragmentStudentReaderViewModel by viewModels()
    override fun onReady(savedInstanceState: Bundle?) {}

    private val book: StudentBook by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentReaderArgs.fromBundle(requireArguments()).book
    }
    private val startPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentReaderArgs.fromBundle(requireArguments()).startPage
    }
    private val lastPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentReaderArgs.fromBundle(requireArguments()).lastPage
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentReaderArgs.fromBundle(requireArguments()).chapter
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
        viewModel.getBook(url = book.book.url).onEach { state ->
            when (state.status) {
                Status.LOADING -> progressBarVisibility(status = true)
                Status.SUCCESS -> loadSuccess(inputStream = state.data!!)
                Status.ERROR -> loadError(message = state.message!!)
                Status.NETWORK_ERROR -> {}
            }
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun loadSuccess(inputStream: InputStream) {
        bookCurrentProgress = book.progress
        for (i in startPage until lastPage) {
            pages.add(i)
        }

        binding().pdfview.fromStream(inputStream)
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

    private fun loadError(message: String) {
        progressBarVisibility(status = false)
        showToast(message = message)
        viewModel.goBack()
    }

    private fun progressBarVisibility(status: Boolean) {
        binding().apply {
            if (status) {
                constraintLayout.hideView()
                pdfview.hideView()
                lottieAnimation.showView()
            } else {
                constraintLayout.showView()
                lottieAnimation.hideView()
                pdfview.showView()
            }
        }
    }

    private fun setOnClickListeners() {
        binding().apply {
            endRead.setOnClickListener(this@FragmentStudentReader)
            saveChanges.setOnClickListener(this@FragmentStudentReader)
            pageLast.setOnClickListener(this@FragmentStudentReader)
            pageFirst.setOnClickListener(this@FragmentStudentReader)
            pageForward.setOnClickListener(this@FragmentStudentReader)
            pageBack.setOnClickListener(this@FragmentStudentReader)
        }
    }

    override fun loadComplete(nbPages: Int) {
        progressBarVisibility(status = false)
    }

    override fun onError(t: Throwable?) {
        progressBarVisibility(status = false)
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
                BookUpdateProgress(progress = progress),
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
}




