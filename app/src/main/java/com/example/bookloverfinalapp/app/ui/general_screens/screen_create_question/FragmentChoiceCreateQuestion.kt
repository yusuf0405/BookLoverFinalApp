package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.models.AddQuestionTypes
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.tuneBottomDialog
import com.example.bookloverfinalapp.app.utils.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.FragmentChoiceQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentChoiceCreateQuestion : DialogFragment() {

    private var _binding: FragmentChoiceQuestionBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private var clickListener: ((AddQuestionTypes) -> Unit)? = null

    override fun onStart() {
        super.onStart()
        tuneBottomDialog()
        tuneLyricsDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoiceQuestionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(com.joseph.ui_core.R.style.ModalPage_Animation)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        questionQuizType.setOnDownEffectClickListener {
            dismiss()
            clickListener?.invoke(AddQuestionTypes.QUIZ)
        }
    }

    companion object {
        fun newInstance(listener: (type: AddQuestionTypes) -> Unit) = FragmentChoiceCreateQuestion()
            .apply { clickListener = listener }
    }
}