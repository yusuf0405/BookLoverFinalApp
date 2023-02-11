package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.FragmentChoiceCreateQuestion
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter.QuestionChapterFingerprint
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.models.AddQuestionTypes
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showOnlyOne
import com.example.bookloverfinalapp.databinding.FragmentAdminChaptersBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.joseph.ui_core.extensions.launchWhenStarted
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class FragmentChaptersForCreateQuestion :
    BaseFragment<FragmentAdminChaptersBinding, FragmentChaptersForCreateQuestionViewModel>(
        FragmentAdminChaptersBinding::inflate
    ), OnLoadCompleteListener, OnErrorListener, OnPageChangeListener {

    @Inject
    lateinit var factory: FragmentChaptersForCreateQuestionViewModelFactory.Factory

    private val bookId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentChaptersForCreateQuestionArgs.fromBundle(requireArguments()).bookId
    }

    override val viewModel: FragmentChaptersForCreateQuestionViewModel by viewModels {
        factory.create(bookId = bookId)
    }

    private val adapter = FingerprintAdapter(
        listOf(
            SearchFingerprint(),
            QuestionChapterFingerprint()
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        chapterRecyclerView.itemAnimator = createAddableItemAnimator()
        chapterRecyclerView.adapter = adapter
        toolbarBlock.sortOptions.isVisible = false
        toolbarBlock.upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            bookFlow.observe(::handleFetchingBook)
            bookPatchFlow.observe(::handleBookPatch)
            bookChaptersFlow.observe(adapter::submitList)
            showChoiceCreateQuestionDialogFlow.observe { showFragmentChoiceCreateQuestion() }
        }
    }

    private fun handleFetchingBook(book: Book) = with(binding()) {
        toolbarBlock.title.text = book.title
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

    private fun handleBookPatch(path: String?) {
        if (path == null) return
        binding().pdfView.fromFile(File(path))
            .onPageChange(this@FragmentChaptersForCreateQuestion)
            .onError(this@FragmentChaptersForCreateQuestion)
            .onLoad(this@FragmentChaptersForCreateQuestion)
            .load()
    }

    override fun loadComplete(nbPages: Int) {
        viewModel.updateBookChapters(binding().pdfView.tableOfContents)
    }

    override fun onError(t: Throwable?) {
        viewModel.navigateBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
    }

    private fun handleNavigateType(types: AddQuestionTypes) {
        when (types) {
            AddQuestionTypes.QUIZ -> viewModel.navigateToCreateQuestionFragment()
        }
    }

    private fun showFragmentChoiceCreateQuestion() = FragmentChoiceCreateQuestion
        .newInstance(listener = ::handleNavigateType)
        .showOnlyOne(parentFragmentManager)

}