package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions.ui

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions.adapter.AdminChapterItemOnClickListener
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions.adapter.AdminQuestionsAdapter
import com.example.bookloverfinalapp.databinding.FragmentAllQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllQuestion :
    BaseFragment<FragmentAllQuestionBinding, FragmentAllQuestionViewModel>(
        FragmentAllQuestionBinding::inflate), AdminChapterItemOnClickListener {

    override val viewModel: FragmentAllQuestionViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val adapter: AdminQuestionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AdminQuestionsAdapter(actionListener = this)
    }

    private val id: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAllQuestionArgs.fromBundle(requireArguments()).id
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAllQuestionArgs.fromBundle(requireArguments()).chapter
    }
    private val title: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentAllQuestionArgs.fromBundle(requireArguments()).title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        onClickListeners()
        observeResource()
    }

    private fun observeResource() {

        viewModel.fetchBookQuestions(id = id, chapter = chapter)

        viewModel.observe(viewLifecycleOwner) { questions ->
            adapter.questions = questions.toMutableList()
        }
    }

    private fun setupUi() {
        binding().apply {
            toolbar.title = title
            toolbar.setTitleTextColor(Color.WHITE)
            adminQuestionsRecyclerView.adapter = adapter
        }
    }

    private fun onClickListeners() {
        binding().apply {
            addQuestionButton.setOnClickListener {
                viewModel.goAddQuestionFragment(id = id, chapter = chapter, title = title)
            }
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun deleteQuestion(id: String, position: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteQuestion(id = id).observe(viewLifecycleOwner) {
                        showToast(R.string.book_question_deleted_successfully)
                    }
                    adapter.deleteQuestion(position = position)
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.login_out_title)
            .setCancelable(true)
            .setMessage(R.string.default_delete_question_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()

        dialog.show()
    }

    override fun goQuestionEditFragment(question: BookQuestion) {
        viewModel.goQuestionEditFragment(question = question,
            title = title,
            id = id,
            chapter = chapter)
    }


}