package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_question

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.showCustomInputAlertDialog
import com.example.bookloverfinalapp.databinding.FragmentAdminEditQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAdminEditQuestion :
    BaseFragment<FragmentAdminEditQuestionBinding, FragmentAdminEditQuestionViewModel>(
        FragmentAdminEditQuestionBinding::inflate
    ) {

    override val viewModel: FragmentAdminEditQuestionViewModel by viewModels()

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

    private fun setupUi() = with(binding()) {
        toolbar.title = title
        setToolbarColor(toolbar = toolbar)
        questionText.setText(question.question)
        answerAButton.text = question.a
        answerBButton.text = question.b
        answerCButton.text = question.c
        answerDButton.text = question.d
        when (question.rightAnswer) {
            ANSWER_A -> answerAButton.isChecked = true
            ANSWER_B -> answerBButton.isChecked = true
            ANSWER_C -> answerCButton.isChecked = true
            ANSWER_D -> answerDButton.isChecked = true
        }
    }

    private fun setOnClickListeners() = with(binding()) {
        answerAButton.setOnDownEffectClickListener {
            showCustomInputAlertDialog(radioButton = answerAButton)
        }
        answerBButton.setOnDownEffectClickListener {
            showCustomInputAlertDialog(radioButton = answerBButton)
        }
        answerCButton.setOnDownEffectClickListener {
            showCustomInputAlertDialog(radioButton = answerCButton)
        }
        answerDButton.setOnDownEffectClickListener {
            showCustomInputAlertDialog(radioButton = answerDButton)
        }
        editQuestionButton.setOnDownEffectClickListener { checkUpdate() }
        toolbar.setNavigationOnClickListener { viewModel.navigateBack() }

    }

    private fun checkUpdate() = with(binding()) {
        val rightAnswer = when {
            answerAButton.isChecked -> ANSWER_A
            answerBButton.isChecked -> ANSWER_B
            answerCButton.isChecked -> ANSWER_C
            else -> ANSWER_D
        }
        if (answerAButton.text.toString() != question.a ||
            answerBButton.text.toString() != question.b ||
            answerCButton.text.toString() != question.c ||
            answerDButton.text.toString() != question.d ||
            rightAnswer != question.rightAnswer ||
            questionText.text.toString() != question.question
        ) updateQuestion(rightAnswer)
        else showInfoSnackBar(getString(R.string.enter_the_change))
    }

    private fun updateQuestion(rightAnswer: String) = with(binding()) {
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
        viewModel.updateQuestion(id = question.id, question = updateQuestion)
//                .observe(viewLifecycleOwner) {
//                    showToast(R.string.book_question_updated_successfully)
//                }
    }

    private companion object {
        const val ANSWER_A = "a"
        const val ANSWER_B = "b"
        const val ANSWER_C = "c"
        const val ANSWER_D = "d"
    }
}