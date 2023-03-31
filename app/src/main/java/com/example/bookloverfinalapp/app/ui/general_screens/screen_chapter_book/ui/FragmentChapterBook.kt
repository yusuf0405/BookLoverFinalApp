package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter.ChapterFingerprint
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint
import com.example.bookloverfinalapp.databinding.FragmentChapterBookBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class FragmentChapterBook :
    BaseFragment<FragmentChapterBookBinding, FragmentStudentChapterBookViewModel>(
        FragmentChapterBookBinding::inflate
    ), OnLoadCompleteListener, OnErrorListener, OnPageChangeListener {

    @Inject
    lateinit var factory: FragmentStudentChapterBookViewModel.Factory
    override val viewModel: FragmentStudentChapterBookViewModel by viewModelCreator {
        factory.create(book = book, patch = path)
    }

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChapterBookArgs.fromBundle(requireArguments()).book
    }

    private val path: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChapterBookArgs.fromBundle(requireArguments()).path
    }

    private val adapter = FingerprintAdapter(
        listOf(
            SearchFingerprint(),
            ChapterFingerprint()
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        toolbarBlock.title.text = book.title
        toolbarBlock.sortOptions.isInvisible = true
        chapterRecyclerView.adapter = adapter
        chapterRecyclerView.itemAnimator = createAddableItemAnimator()
        toolbarBlock.upButton.setOnClickListener { viewModel.navigateBack() }
        setupPdfView()
    }

    private fun setupPdfView() {
        binding().pdfView.fromFile(File(path))
            .onPageChange(this@FragmentChapterBook)
            .onError(this@FragmentChapterBook)
            .onLoad(this@FragmentChapterBook)
            .load()
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            allBookChapters
                .filterNotNull()
                .onEach { if (it.isEmpty()) viewModel.navigate() }
                .filter { it.isNotEmpty() }
                .observe(adapter::submitList)
        }
    }

    private fun createAddableItemAnimator() =
        AddableItemAnimator(SimpleCommonAnimator()).also { anim ->
            anim.addViewTypeAnimation(
                R.layout.item_chapter,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_search_view,
                SlideInTopCommonAnimator()
            )
            anim.addDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
            anim.removeDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
        }

    override fun loadComplete(nbPages: Int) {
        viewModel.updateChapters(binding().pdfView.tableOfContents)
    }

    override fun onError(t: Throwable?) {
        viewModel.navigateBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        viewModel.updateBookLastPage(pageCount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
