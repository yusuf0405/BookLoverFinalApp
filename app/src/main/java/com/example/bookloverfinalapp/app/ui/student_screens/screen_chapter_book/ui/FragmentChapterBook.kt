package com.example.bookloverfinalapp.app.ui.student_screens.screen_chapter_book.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.student_screens.screen_chapter_book.adapters.ChapterAdapter
import com.example.bookloverfinalapp.app.ui.student_screens.screen_chapter_book.adapters.ChapterItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.databinding.FragmentChapterBookBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shockwave.pdfium.PdfDocument
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FragmentChapterBook :
    BaseFragment<FragmentChapterBookBinding, FragmentStudentChapterBookViewModel>(
        FragmentChapterBookBinding::inflate), ChapterItemOnClickListener,
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener {
    override val viewModel: FragmentStudentChapterBookViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChapterBookArgs.fromBundle(requireArguments()).book
    }
    private val adapter: ChapterAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChapterAdapter(actionListener = this)
    }

    private var chapterList = mutableListOf<PdfDocument.Bookmark>()
    private var lastPageBook = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        onClickListeners()
        observeResource()
    }

    private fun observeResource() {
        loadSuccess()
    }


    private fun loadSuccess() {
        binding().pdfView.fromFile(File(book.book))
            .onPageChange(this@FragmentChapterBook)
            .onError(this@FragmentChapterBook)
            .onLoad(this@FragmentChapterBook)
            .load()
    }

    private fun setupUi() {
        binding().apply {
            toolbar.title = book.title
            toolbar.setTitleTextColor(Color.WHITE)
            chapterRecyclerView.adapter = adapter
        }
    }

    private fun onClickListeners() {
        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun goReadPdfFragment(chapter: PdfDocument.Bookmark, position: Int) {
        val lastPageIndex = position + 1
        val lastPage = if (lastPageIndex == chapterList.size) lastPageBook
        else chapterList[lastPageIndex].pageIdx.toInt()
        viewModel.goReaderFragment(book = book,
            startPage = chapter.pageIdx.toInt(),
            lastPage = lastPage, chapter = position + 1)
    }

    override fun loadComplete(nbPages: Int) {
        adapter.isReading = book.isReadingPages.toTypedArray()
        adapter.chapters = binding().pdfView.tableOfContents
        chapterList = binding().pdfView.tableOfContents
    }

    override fun onError(t: Throwable?) {
        viewModel.goBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        lastPageBook = pageCount
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}
