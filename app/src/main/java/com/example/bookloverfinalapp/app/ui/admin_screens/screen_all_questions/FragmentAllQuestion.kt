package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.ui.adapter.QuestionModel
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentAllQuestionBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllQuestion :
    BaseFragment<FragmentAllQuestionBinding, FragmentAllQuestionViewModel>(
        FragmentAllQuestionBinding::inflate), ItemOnClickListener {

    override val viewModel: FragmentAllQuestionViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
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

        viewModel.collect(viewLifecycleOwner) { questions ->
            adapter.map(questions.swapElements().toMutableList())
        }
    }

    private fun setupUi() {
        binding().apply {
            toolbar.title = title
            setToolbarColor(toolbar)
            adminQuestionsRecyclerView.adapter = adapter
        }
    }

    private fun onClickListeners() {
        binding().apply {
            PushDownAnim.setPushDownAnimTo(addQuestionButton).setOnClickListener {
                viewModel.goAddQuestionFragment(id = id, chapter = chapter, title = title)
            }
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun showAnotherFragment(item: ItemUi) {
        viewModel.goQuestionEditFragment(question = viewModel.adapterMapper.map(item as QuestionModel),
            title = title,
            id = id,
            chapter = chapter)
    }

    override fun deleteItem(item: ItemUi, position: Int) {
        val questionUi = item as QuestionModel
        val id = questionUi.id
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteQuestion(id = id).observe(viewLifecycleOwner) {
                        showToast(R.string.book_question_deleted_successfully)
                    }
                    adapter.deleteItem(position = position)
                }
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

    override fun updateItem(item: ItemUi, position: Int) = Unit


}