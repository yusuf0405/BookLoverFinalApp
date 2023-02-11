package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.FragmentGoToPage
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FragmentReader :
    BaseFragment<FragmentReaderBinding, FragmentReaderViewModel>(FragmentReaderBinding::inflate),
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener {

    override val viewModel: FragmentReaderViewModel by viewModels()

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

    private val chapterTitle: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).chapterTitle
    }

    private val isNightMode: Boolean by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
    }
    private val pdfPages = arrayListOf<Int>()
    private val pages = arrayListOf<Int>()
    private var progress = 0
    private var bookCurrentProgress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setOnClickListeners()
        observeResource()
    }

    private fun setupUi() = with(binding()) {
        pdfview.useBestQuality(true)
        title.text = chapterTitle
        setupPdfView()
    }

    private fun observeResource()= with(viewModel) {
        launchWhenViewStarted {  }
    }

    private fun setupPdfView() {
        bookCurrentProgress = book.progress
        for (i in startPage until lastPage) {
            pdfPages.add(i)
        }

        val pageCount = lastPage - startPage
        for (i in 0 until pageCount) {
            val page = startPage + i
            pages.add(page)

        }
        binding().pdfview.fromFile(File(path))
            .pages(*pdfPages.toIntArray())
            .swipeHorizontal(true)
            .pageSnap(true)
            .fitEachPage(false)
            .autoSpacing(true)
            .pageFling(true)
            .enableDoubletap(true)
            .pageFitPolicy(FitPolicy.BOTH)
            .onPageChange(this)
            .onLoad(this)
            .enableAnnotationRendering(true)
            .nightMode(isNightMode)
            .load()
    }

    private fun setOnClickListeners() = with(binding()) {
        pageEnterField.setOnDownEffectClickListener {
            showFragmentGoToPage(currentPage = pageEnterField.text.toString().toInt())
        }
        endRead.setOnDownEffectClickListener {
            if (NavigationManager().isOnline(context = requireContext())) {
                viewModel.goQuestionFragment(
                    book = book,
                    chapter = chapter,
                    path = path
                )
            } else showErrorSnackbar(getString(R.string.network_error))
        }
        pageLast.setOnDownEffectClickListener {
            setPdfViewPage(pdfPages.size)
            if (bookCurrentProgress <= progress) endRead.show()
        }
        pageFirst.setOnDownEffectClickListener { setPdfViewPage(0) }
        pageForward.setOnDownEffectClickListener { setPdfViewPage(binding().pdfview.currentPage + 1) }
        pageBack.setOnDownEffectClickListener { setPdfViewPage(binding().pdfview.currentPage - 1) }
        upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    override fun loadComplete(nbPages: Int) = Unit

    override fun onError(t: Throwable?) {
        showToast(R.string.generic_error)
        viewModel.navigateBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        binding().apply {
            pageEnterField.text = pdfPages[page].toString()
            if (page == pageCount - 1) {
                if (bookCurrentProgress <= progress) endRead.show()
            } else endRead.hide()
            progress = pdfPages[page]
        }

    }

    private fun saveChanges() {
        if (bookCurrentProgress > progress) return
        viewModel.updateBookReadingProgress(
            id = book.objectId,
            progress = progress,
            currentDayProgress = progress - bookCurrentProgress
        )
        bookCurrentProgress = progress
    }

    private fun setPdfViewPage(page: Int) = binding().pdfview.jumpTo(page)

    private fun handleNavigateToNewPage(newPage: Int) {
        if (newPage in startPage..lastPage) {
            pages.forEach { oldPage ->
                if (oldPage == newPage) setPdfViewPage(pages.indexOf(oldPage))
            }
        } else showInfoSnackBar(getString(R.string.have_gone_beyond))
    }

    private fun showFragmentGoToPage(currentPage: Int) {
        FragmentGoToPage.newInstance(
            title = "${getString(R.string.go_to_pages)} $startPage ... ${lastPage - 1}",
            currentPage = currentPage,
            listener = ::handleNavigateToNewPage
        ).show(requireActivity().supportFragmentManager, ModalPage.TAG)
    }

    override fun onPause() {
        super.onPause()
        saveChanges()
    }
}




