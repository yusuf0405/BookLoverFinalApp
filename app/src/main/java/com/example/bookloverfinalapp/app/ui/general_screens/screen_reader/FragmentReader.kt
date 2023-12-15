package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.SettingSelectionFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.dialog_option.FragmentReaderOption
import com.example.bookloverfinalapp.app.utils.extensions.dismissPlayerOverlay
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showPlayerOverlay
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.example.bookloverfinalapp.R as MainRes

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
        isHaveToolbar = true
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        pdfview.useBestQuality(true)
        includeDefaultToolbar.title.text = chapterTitle
        dismissPlayerOverlay()
        requireActivity().findViewById<BottomNavigationView>(MainRes.id.bottomNavigationView).hide()
        lifecycleScope.launch {
            val text = extractTextUsingItextg()
            withContext(Dispatchers.Main) {
                textpdfview.text = text
            }
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            requireContext().dataStore.data.collectLatest { pref ->
                val orientation =
                    pref[intPreferencesKey(SettingSelectionFragment.ORIENTATION_KEY)] ?: 1
                setupPdfView(orientation)
            }
        }
    }

    private fun setupPdfView(orientation: Int) {
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
            .swipeHorizontal(orientation == 0)
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
            .onRender { binding().pdfview.fitToWidth(binding().pdfview.currentPage) }
            .load()
    }

    private suspend fun extractTextUsingItextg(): String = withContext(Dispatchers.Default) {
        try {
            var parsedText = ""
            val reader = PdfReader(path)
            val n: Int = reader.numberOfPages
            for (i in 0 until n) {
                parsedText = """$parsedText${
                    PdfTextExtractor.getTextFromPage(reader, i + 1).trim { it <= ' ' }
                }
            """.trimIndent()
            }
            reader.close()
            parsedText
        } catch (e: Exception) {
            "Empty"
        }
    }


    private fun setOnClickListeners() = with(binding()) {
        pageEnterField.setOnDownEffectClickListener {
            showFragmentGoToPage(currentPage = fetchCurrentPage())
        }
        setting.setOnDownEffectClickListener { showSettingModalPage() }
        option.setOnDownEffectClickListener { showReaderOptionModalPage() }
//        endRead.setOnDownEffectClickListener {
//            if (NavigationManager().isOnline(context = requireContext())) {
//                viewModel.navigateToQuestionFragment(book, chapter, path)
//            } else showErrorSnackbar(getString(R.string.network_error))
//        }
        pageLast.setOnDownEffectClickListener {
            setPdfViewPage(pdfPages.size)
//            if (bookCurrentProgress <= progress) endRead.show()
        }
        pageFirst.setOnDownEffectClickListener { setPdfViewPage(0) }
        pageForward.setOnDownEffectClickListener { setPdfViewPage(binding().pdfview.currentPage + 1) }
        pageBack.setOnDownEffectClickListener { setPdfViewPage(binding().pdfview.currentPage - 1) }
        includeDefaultToolbar.toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
    }

    override fun loadComplete(nbPages: Int) = Unit

    override fun onError(t: Throwable?) {
        showToast(R.string.generic_error)
        viewModel.navigateBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        binding().apply {
            pageEnterField.text = pdfPages[page].toString()
//            if (page == pageCount - 1) {
//                if (bookCurrentProgress <= progress) endRead.show()
//            } else endRead.hide()
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


    private fun fetchCurrentPage() = binding().pageEnterField.text.toString().toInt()

    private fun showReaderOptionModalPage() = FragmentReaderOption.newInstance(
        title = getString(R.string.options),
        isToolbarState = binding().includeDefaultToolbar.root.isVisible,
        goToPageListener = { showFragmentGoToPage(currentPage = fetchCurrentPage()) },
        goToBookInfoListener = { viewModel.navigateToBookInfoFragment(book.bookId) },
        hideOrShowToolbarListener = { hideOrShowToolbar() }
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun hideOrShowToolbar() = with(binding()) {
        includeDefaultToolbar.root.isVisible = !includeDefaultToolbar.root.isVisible
    }

    private fun showSettingModalPage() = FragmentSetting.newInstance(
        title = getString(R.string.setting),
        isBookReaderSettingShow = true,
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

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

    override fun onDestroyView() {
        super.onDestroyView()
        showPlayerOverlay()
    }
}