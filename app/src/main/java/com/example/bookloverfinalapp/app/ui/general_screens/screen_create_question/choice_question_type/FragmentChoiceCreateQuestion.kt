package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choice_question_type

import android.os.Bundle
import android.view.View
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models.CreateQuestionType
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentChoiceQuestionBinding
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentChoiceCreateQuestion :
    BaseBindingFragment<FragmentChoiceQuestionBinding>(FragmentChoiceQuestionBinding::inflate) {

    private var clickListener: ((CreateQuestionType) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        questionQuizType.setOnDownEffectClickListener {
            dismissModalPage()
            clickListener?.invoke(CreateQuestionType.QUIZ)
        }
        trueFalseBlock.setOnDownEffectClickListener {
            dismissModalPage()
            clickListener?.invoke(CreateQuestionType.TRUE_FALSE)
        }
    }

    companion object {
        fun newInstance(listener: (type: CreateQuestionType) -> Unit) =
            FragmentChoiceCreateQuestion()
                .run {
                    clickListener = listener
                    ModalPage.Builder()
                        .fragment(this)
                        .build()
                }
    }
}