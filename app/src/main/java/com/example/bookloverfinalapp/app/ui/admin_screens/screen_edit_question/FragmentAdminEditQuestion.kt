package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_question

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.utils.extensions.showCustomInputAlertDialog
import com.example.bookloverfinalapp.databinding.FragmentAdminEditQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAdminEditQuestion :
    BaseFragment<FragmentAdminEditQuestionBinding, FragmentAdminEditQuestionViewModel>(
        FragmentAdminEditQuestionBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentAdminEditQuestionViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val question: BookQuestion by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAdminEditQuestionArgs.fromBundle(requireArguments()).question
    }
    private val title: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAdminEditQuestionArgs.fromBundle(requireArguments()).title
    }
    private val id: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAdminEditQuestionArgs.fromBundle(requireArguments()).id
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAdminEditQuestionArgs.fromBundle(requireArguments()).chapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setOnClickListeners()
    }

    private fun setupUi() {
        binding().apply {
            toolbar.title = title
            toolbar.setTitleTextColor(Color.WHITE)
            questionText.setText(question.question)
            answerAButton.text = question.a
            answerBButton.text = question.b
            answerCButton.text = question.c
            answerDButton.text = question.d
            when (question.rightAnswer) {
                "a" -> answerAButton.isChecked = true
                "b" -> answerBButton.isChecked = true
                "c" -> answerCButton.isChecked = true
                "d" -> answerDButton.isChecked = true
            }
        }
    }

    private fun setOnClickListeners() {
        binding().apply {
            answerAButton.setOnClickListener(this@FragmentAdminEditQuestion)
            answerBButton.setOnClickListener(this@FragmentAdminEditQuestion)
            answerCButton.setOnClickListener(this@FragmentAdminEditQuestion)
            answerDButton.setOnClickListener(this@FragmentAdminEditQuestion)
            editQuestionButton.setOnClickListener(this@FragmentAdminEditQuestion)
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().answerAButton -> showCustomInputAlertDialog(binding().answerAButton,
                binding().answerAButton.text.toString())
            binding().answerBButton -> showCustomInputAlertDialog(binding().answerBButton,
                binding().answerBButton.text.toString())
            binding().answerCButton -> showCustomInputAlertDialog(binding().answerCButton,
                binding().answerCButton.text.toString())
            binding().answerDButton -> showCustomInputAlertDialog(binding().answerDButton,
                binding().answerDButton.text.toString())
            binding().editQuestionButton -> chekUpdate()
        }
    }

    private fun chekUpdate() {
        binding().apply {
            val rightAnswer =
                when {
                    answerAButton.isChecked -> "a"
                    answerBButton.isChecked -> "b"
                    answerCButton.isChecked -> "c"
                    else -> "d"
                }
            if (answerAButton.text.toString() != question.a ||
                answerBButton.text.toString() != question.b ||
                answerCButton.text.toString() != question.c ||
                answerDButton.text.toString() != question.d ||
                rightAnswer != question.rightAnswer ||
                questionText.text.toString() != question.question
            ) updateQuestion(rightAnswer)
            else showToast(R.string.enter_the_change)

        }

    }

    private fun updateQuestion(rightAnswer: String) {
        binding().apply {
            val updateQuestion = AddBookQuestion(
                question = questionText.text.toString(),
                a = answerAButton.text.toString(),
                b = answerBButton.text.toString(),
                c = answerCButton.text.toString(),
                d = answerDButton.text.toString(),
                bookId = id,
                rightAnswer = rightAnswer,
                chapter = chapter.toString()
            )
            viewModel.updateQuestion(id = question.id, question = updateQuestion).observe(viewLifecycleOwner) {
                    showToast(R.string.book_question_updated_successfully)
                }
        }

    }
}