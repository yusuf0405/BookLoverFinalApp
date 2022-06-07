package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter.ChapterAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter.ChapterItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
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

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChapterBookArgs.fromBundle(requireArguments()).book
    }

    private val path: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChapterBookArgs.fromBundle(requireArguments()).path
    }

    private val adapter: ChapterAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ChapterAdapter(actionListener = this)
    }

    private var chapterList = mutableListOf<PdfDocument.Bookmark>()
    private var lastPageBook = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        loadSuccess()
        binding().toolbar.setNavigationOnClickListener { viewModel.goBack() }
    }


    private fun loadSuccess() {
        binding().pdfView.fromFile(File(path))
            .onPageChange(this@FragmentChapterBook)
            .onError(this@FragmentChapterBook)
            .onLoad(this@FragmentChapterBook)
            .load()
    }

    private fun setupUi() {
        binding().apply {
            setToolbarColor(toolbar = toolbar)
            toolbar.title = book.title
            chapterRecyclerView.adapter = adapter
        }
    }


    override fun goReadPdfFragment(chapter: PdfDocument.Bookmark, position: Int) {
        val lastPageIndex = position + 1
        val lastPage = if (lastPageIndex == chapterList.size) lastPageBook
        else chapterList[lastPageIndex].pageIdx.toInt()
        viewModel.goReaderFragment(book = book,
            chapter = position + 1,
            startPage = chapter.pageIdx.toInt(),
            lastPage = lastPage, path = path)
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

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}
