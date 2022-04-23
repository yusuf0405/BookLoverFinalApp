package com.example.bookloverfinalapp.app.ui.student_screens.screen_questions

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.launchWhenStarted
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentBookQuestionBinding
import com.example.domain.models.Status
import com.example.domain.models.book.BookQuestion
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class FragmentStudentBookQuestion :
    BaseFragment<FragmentStudentBookQuestionBinding, FragmentStudentBookQuestionViewModel>(
        FragmentStudentBookQuestionBinding::inflate), View.OnClickListener {
    override val viewModel: FragmentStudentBookQuestionViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val book: StudentBook by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentBookQuestionArgs.fromBundle(requireArguments()).book
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentBookQuestionArgs.fromBundle(requireArguments()).chapter
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

    private fun setOnClickListeners() {
        binding().apply {
            answerAButton.setOnClickListener(this@FragmentStudentBookQuestion)
            answerBButton.setOnClickListener(this@FragmentStudentBookQuestion)
            answerCButton.setOnClickListener(this@FragmentStudentBookQuestion)
            answerDButton.setOnClickListener(this@FragmentStudentBookQuestion)
            btnNextTest.setOnClickListener(this@FragmentStudentBookQuestion)
            btnPrevTest.setOnClickListener(this@FragmentStudentBookQuestion)
        }
    }

    private fun observeResource() {
        viewModel.getAllChapterQuestions(id = book.bookId, chapter = chapter).onEach { state ->
            when (state.status) {
                Status.LOADING -> loadingDialog.show()
                Status.SUCCESS -> responseSuccess(list = state.data!!)
                Status.ERROR -> responseError(message = state.message!!)
                else -> loadingDialog.dismiss()
            }
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun responseSuccess(list: List<BookQuestion>) {
        loadingDialog.dismiss()
        listQuestions = list.toMutableList()
        setupUi(listQuestions[index])
    }

    private fun responseError(message: String) {
        if (message == "No questions!!") {
            showToast(message = getString(R.string.no_book_questions))
            updateObject()
        }
        loadingDialog.dismiss()
    }

    private fun setupUi(book: BookQuestion) {
        binding().apply {
            restart()
            rightAnswer = book.rightAnswer
            questionText.text = book.question
            answerAButton.text = book.a
            answerBButton.text = book.b
            answerCButton.text = book.c
            answerDButton.text = book.d
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().answerAButton -> answer = "a"
            binding().answerBButton -> answer = "b"
            binding().answerCButton -> answer = "c"
            binding().answerDButton -> answer = "d"
            binding().btnNextTest -> chekAnswer()
            binding().btnPrevTest -> chekPrevQuestion()
        }
    }

    private fun chekAnswer() {
        if (rightAnswer == answer) {
            chekNextQuestion()
        } else {
            viewModel.goChapterFragment(book = book)
            showToast(message = getString(R.string.answer_is_error))
        }
    }

    private fun chekPrevQuestion() {
        if (index > 0) {
            index -= 1
            if (index == 0) binding().btnPrevTest.hideView()
            setupUi(book = listQuestions[index])
        }
    }

    private fun chekNextQuestion() {
        index += 1
        if (index < listQuestions.size) {
            showToast(message = getString(R.string.answer_is_success))
            binding().btnPrevTest.showView()
            setupUi(book = listQuestions[index])
        } else {
            updateObject()
            showToast(message = getString(R.string.survey_is_success))
        }
    }

    private fun updateObject() {
        val query = ParseQuery.getQuery<ParseObject>("BooksThatRead")
        query.getInBackground(book.objectId) { objects: ParseObject, e: ParseException? ->
            if (e == null) {
                val list = book.isReadingPages.toMutableList()
                if (chapter < list.size) {
                    list[chapter] = true
                    book.isReadingPages = list
                    objects.put("isReadingPages", list.toList())
                    objects.saveInBackground()
                }
                if (chapter > book.chaptersRead) {
                    objects.put("chaptersRead", chapter)
                    objects.saveInBackground()
                }
                viewModel.goChapterFragment(book = book)
            } else {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun restart() {
        binding().apply {
            answer = ""
            answerAButton.isChecked = false
            answerBButton.isChecked = false
            answerCButton.isChecked = false
            answerDButton.isChecked = false
        }
    }

}