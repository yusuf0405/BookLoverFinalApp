package com.example.bookloverfinalapp.app.ui.general_screens.screen_questions

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentBookQuestionBinding
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentBookQuestion :
    BaseFragment<FragmentBookQuestionBinding, FragmentBookQuestionViewModel>(
        FragmentBookQuestionBinding::inflate
    ) {
    override val viewModel: FragmentBookQuestionViewModel by viewModels()

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentBookQuestionArgs.fromBundle(requireArguments()).book
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentBookQuestionArgs.fromBundle(requireArguments()).chapter
    }
    private val path: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentBookQuestionArgs.fromBundle(requireArguments()).path
    }
    private var index = 0
    private var answer: String = ""
    private var rightAnswer: String? = null
    private var listQuestions = mutableListOf<BookQuestion>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeResource()
    }

    private fun setOnClickListeners() = with(binding()) {
        answerAButton.setOnDownEffectClickListener { answer = "a" }
        answerBButton.setOnDownEffectClickListener { answer = "b" }
        answerCButton.setOnDownEffectClickListener { answer = "c" }
        answerDButton.setOnDownEffectClickListener { answer = "d" }
        btnNextTest.setOnDownEffectClickListener { chekAnswer() }
        btnPrevTest.setOnDownEffectClickListener { checkPrevQuestion() }
        includeErrorBlock.retryButton.setOnDownEffectClickListener {
            viewModel.getAllChapterQuestions(id = book.bookId, chapter = chapter.toString())
        }
        includeErrorBlock.backButton.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            viewModel.getAllChapterQuestions(id = book.bookId, chapter = chapter.toString())
            chapterAllQuestionsFlow.observe { questions ->
                if (questions.isEmpty()) {
                    showInfoSnackBar(message = getString(R.string.no_book_questions))
                    updateObject()
                    return@observe
                }
                listQuestions = questions.swapElements().toMutableList()
                setupViews(listQuestions[index])
            }
            isErrorFlow.observe(::handleError)
            isStartFetchQuestionsFlow.observe(::handleStartFetchQuestions)
        }
    }

    private fun handleError(message: String) = with(binding()) {
        progressBar.isVisible = false
        includeErrorBlock.errorMessage.text = message
        errorBlock.isVisible = true
    }

    private fun handleStartFetchQuestions(isStart: Boolean) = with(binding()) {
        progressBar.isVisible = isStart
        errorBlock.isVisible = !isStart
    }

    private fun setupViews(bookDomain: BookQuestion) = binding().apply {
        progressBar.hide()
        container.show()
        restart()
        rightAnswer = bookDomain.rightAnswer
        questionText.text = bookDomain.question
        answerAButton.text = bookDomain.a
        answerBButton.text = bookDomain.b
        answerCButton.text = bookDomain.c
        answerDButton.text = bookDomain.d
    }

    private fun chekAnswer() {
        when {
            answer == "" -> showToast(message = "Выберите ответ")
            rightAnswer == answer -> checkNextQuestion()
            else -> {
                viewModel.navigateToChapterFragment(book = book, path = path)
                showToast(message = getString(R.string.answer_is_error))
            }
        }
    }

    private fun checkPrevQuestion() {
        if (index > 0) {
            index -= 1
            if (index == 0) binding().btnPrevTest.isInvisible = true
            setupViews(bookDomain = listQuestions[index])
        }
    }

    private fun checkNextQuestion() {
        index += 1
        if (index < listQuestions.size) {
            showToast(message = getString(R.string.answer_is_success))
            binding().btnPrevTest.show()
            setupViews(bookDomain = listQuestions[index])
        } else {
            updateObject()
            showToast(message = getString(R.string.survey_is_success))
        }
    }

    private fun updateObject() {
        val isReadingPages = book.isReadingPages.toMutableList()
        if (chapter < isReadingPages.size) {
            isReadingPages[chapter] = true
            book.isReadingPages = isReadingPages
        }
        viewModel.updateChapters(
            id = book.objectId,
            chapters = chapter,
            isReadingPages = isReadingPages,
            book = book,
            path = path
        )
    }


    private fun restart() = binding().apply {
        answer = ""
        answerAButton.isChecked = false
        answerBButton.isChecked = false
        answerCButton.isChecked = false
        answerDButton.isChecked = false
    }
}