package com.example.bookloverfinalapp.app.ui.admin_screens.screen_add_question

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.showCustomInputAlertDialog
import com.example.bookloverfinalapp.databinding.FragmentAddQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAddQuestion : BaseFragment<FragmentAddQuestionBinding, FragmentAddQuestionViewModel>(
    FragmentAddQuestionBinding::inflate
), View.OnClickListener {

    override val viewModel: FragmentAddQuestionViewModel by viewModels()

    private val id: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAddQuestionArgs.fromBundle(requireArguments()).id
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAddQuestionArgs.fromBundle(requireArguments()).chapter
    }

    private val title: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAddQuestionArgs.fromBundle(requireArguments()).title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding().apply {
            answerAButton.setOnDownEffectClickListener(this@FragmentAddQuestion)
            answerBButton.setOnDownEffectClickListener(this@FragmentAddQuestion)
            answerCButton.setOnDownEffectClickListener(this@FragmentAddQuestion)
            answerDButton.setOnDownEffectClickListener(this@FragmentAddQuestion)
            addBookQuestionButton.setOnDownEffectClickListener(this@FragmentAddQuestion)
            toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
        }
    }

    private fun setupUi() {
        binding().apply {
            toolbar.title = title
            setToolbarColor(toolbar = toolbar)
        }
    }

    private fun addQuestion() {
        binding().apply {
            when {
                answerAButton.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                answerBButton.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                answerCButton.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                answerDButton.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                questionText.text.isBlank() -> showToast(R.string.fill_in_all_fields)

                else -> {
                    val rightAnswer =
                        when {
                            answerAButton.isChecked -> "a"
                            answerBButton.isChecked -> "b"
                            answerCButton.isChecked -> "c"
                            else -> "d"
                        }
                    val question = AddBookQuestion(
                        question = questionText.text.toString(),
                        a = answerAButton.text.toString(),
                        b = answerBButton.text.toString(),
                        c = answerCButton.text.toString(),
                        d = answerDButton.text.toString(),
                        bookId = id,
                        rightAnswer = rightAnswer,
                        chapter = chapter.toString()
                    )
                    viewModel.addNewQuestionBook(question = question)
//                        .observe(viewLifecycleOwner) {
//                        showToast(R.string.book_question_added_successfully)
//                    }

                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
//            binding().answerAButton -> showCustomInputAlertDialog(
//                binding().answerAButton,
//            )
//            binding.answerBButton -> showCustomInputAlertDialog(
//                binding.answerBButton,
//            )
//            binding.answerCButton -> showCustomInputAlertDialog(
//                binding.answerCButton,
//            )
//            binding.answerDButton -> showCustomInputAlertDialog(
//                binding.answerDButton,
//            )
//            binding.addBookQuestionButton -> addQuestion()
        }
    }

}