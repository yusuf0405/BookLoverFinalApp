package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.input_answer

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentInputAnswerDialogBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage


class FragmentInputAnswerDialog :
    BaseBindingFragment<FragmentInputAnswerDialogBinding>(FragmentInputAnswerDialogBinding::inflate) {

    private var setOnConfirmClickListener: ((String, Boolean, AnswerVariants) -> Unit)? = null
    private var currentAnswerIsCorrect = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding()) {
        val answerVariant = arguments?.getString(ANSWER_VARIANT_KEY) ?: String()
        val answerIsCorrect = arguments?.getBoolean(ANSWER_IS_CORRECT_KEY) ?: false
        switchCorrectAnswer.isChecked = answerIsCorrect
        answerInput.setText(answerVariant)

    }

    private fun setOnClickListeners() = with(binding()) {
        val answerVariantType =
            arguments?.getSerializable(ANSWER_VARIANT_TYPE_KEY) as? AnswerVariants?
        switchCorrectAnswer.setOnCheckedChangeListener { _, isChecked ->
            currentAnswerIsCorrect = isChecked
        }
        confirmButton.setOnDownEffectClickListener {
            val answer = answerInput.text.toString()
            if (answer.isEmpty()) {
                showErrorSnackbar(getString(R.string.fill_in_all_fields))
                return@setOnDownEffectClickListener
            }
            setOnConfirmClickListener?.invoke(
                answer,
                currentAnswerIsCorrect,
                answerVariantType ?: AnswerVariants.A
            )
            dismissModalPage()
        }
    }


    companion object {
        private const val ANSWER_VARIANT_KEY = "ANSWER_VARIANT_KEY"
        private const val ANSWER_VARIANT_TYPE_KEY = "ANSWER_VARIANT_TYPE_KEY"
        private const val ANSWER_IS_CORRECT_KEY = "ANSWER_IS_CORRECT_KEY"

        fun newInstance(
            answerVariant: String,
            answerIsCorrect: Boolean,
            answerVariantsType: AnswerVariants,
            setOnConfirmClickListener: (String, Boolean, AnswerVariants) -> Unit
        ) = FragmentInputAnswerDialog().run {
            arguments = bundleOf(
                ANSWER_VARIANT_KEY to answerVariant,
                ANSWER_VARIANT_TYPE_KEY to answerVariantsType,
                ANSWER_IS_CORRECT_KEY to answerIsCorrect
            )
            this.setOnConfirmClickListener = setOnConfirmClickListener
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}

enum class AnswerVariants(val value: String) {
    A("a"),
    B("b"),
    C("c"),
    D("d")
}