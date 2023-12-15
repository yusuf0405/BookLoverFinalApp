package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_point.FragmentChoicePointDialog
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.FragmentChooseQuestionPoster
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_time.FragmentChooseTimeDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.input_answer.AnswerVariants
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.input_answer.FragmentInputAnswerDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models.CreateQuestionType
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentCreateNewQuestionBinding
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.extensions.toDp
import com.joseph.core.extensions.showRoundedImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FragmentCreateNewQuestion :
    BaseFragment<FragmentCreateNewQuestionBinding, FragmentCreateNewQuestionViewModel>(
        FragmentCreateNewQuestionBinding::inflate
    ) {

    override val viewModel: FragmentCreateNewQuestionViewModel by viewModels()

    private val type: CreateQuestionType by lazy(LazyThreadSafetyMode.NONE) {
        FragmentCreateNewQuestionArgs.fromBundle(requireArguments()).type
    }


    private var minutes: Int = 0
    private var seconds: Int = 0
    private var point: Int = 0
    private var answerVariantA = String()
    private var answerVariantB = String()
    private var answerVariantC = String()
    private var answerVariantD = String()
    private var correctAnswerVariant = String()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() {
        when (type) {
            CreateQuestionType.QUIZ -> setupQuizViews()
            CreateQuestionType.TRUE_FALSE -> setupTrueFalseViews()
        }
    }

    private fun setupQuizViews() = with(binding()) {
        quizBlock.show()
    }

    private fun setupTrueFalseViews() = with(binding()) {
        trueFalseBlock.show()
    }

    private fun setOnClickListeners() = with(binding()) {
        addCoverImageBlock.setOnClickListener { showFragmentChooseQuestionPoster() }
        questionPoster.setOnClickListener { showFragmentChooseQuestionPoster() }
        questionTimeLimitContainer.setOnDownEffectClickListener { showFragmentChooseTimeDialog() }
        choosePoint.setOnDownEffectClickListener { showFragmentChoicePointDialog() }
        answerA.setOnDownEffectClickListener {
            showFragmentInputAnswerDialog(
                currentAnswer = answerVariantA,
                answerIsCorrect = AnswerVariants.A.value == correctAnswerVariant,
                answerVariantsType = AnswerVariants.A
            )
        }
        answerB.setOnDownEffectClickListener {
            showFragmentInputAnswerDialog(
                currentAnswer = answerVariantB,
                answerIsCorrect = AnswerVariants.B.value == correctAnswerVariant,
                answerVariantsType = AnswerVariants.B
            )
        }
        answerC.setOnDownEffectClickListener {
            showFragmentInputAnswerDialog(
                currentAnswer = answerVariantC,
                answerIsCorrect = AnswerVariants.C.value == correctAnswerVariant,
                answerVariantsType = AnswerVariants.C
            )
        }
        answerD.setOnDownEffectClickListener {
            showFragmentInputAnswerDialog(
                currentAnswer = answerVariantD,
                answerIsCorrect = AnswerVariants.D.value == correctAnswerVariant,
                answerVariantsType = AnswerVariants.D
            )
        }
    }

    private fun pickImageFileInStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickQuestionPoster.launch(intent)
    }

    private val resultPickQuestionPoster =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val data: Intent? = result.data
            data?.data?.let(::handlePickPosterResult)
        }

    private fun handlePickPosterResult(uri: Uri) = with(binding()) {
        requireContext().showRoundedImage(
            roundedSize = 12.toDp,
            imageUri = uri,
            questionPoster
        )
        showPosterImage()
    }

    private fun handleChoiceTime(checkedMinutes: Int, checkedSeconds: Int) {
        minutes = checkedMinutes
        seconds = checkedSeconds
        val minuteText = if (minutes > 0) "$minutes m " else String()
        val timeText = "$minuteText$seconds sec"
        binding().questionTimeLimitText.text = timeText
    }

    private fun handleChoicePoint(checkedPoint: Int) {
        point = checkedPoint
        val pointText = "$checkedPoint pt"
        binding().questionPointSizeText.text = pointText
    }

    private fun handleTypedAnswer(
        answer: String,
        isCorrect: Boolean,
        answerVariantsType: AnswerVariants
    ) = with(binding()) {
        if (isCorrect) correctAnswerVariant = answerVariantsType.value
        when (answerVariantsType) {
            AnswerVariants.A -> {
                answerVariantA = answer
                answerVariantATextView.text = answer
            }
            AnswerVariants.B -> {
                answerVariantB = answer
                answerVariantBTextView.text = answer
            }
            AnswerVariants.C -> {
                answerVariantC = answer
                answerVariantCTextView.text = answer
            }
            AnswerVariants.D -> {
                answerVariantD = answer
                answerVariantDTextView.text = answer
            }
        }
    }

    private fun onNetworkPosterSelectedListener(posterUrl: String) = with(binding()) {
        requireContext().showRoundedImage(roundedSize = 12.toDp, posterUrl, questionPoster)
        showPosterImage()
    }

    private fun onLocalPosterSelectedListener(posterUri: String) = with(binding()) {
        requireContext().showRoundedImage(roundedSize = 12.toDp, File(posterUri), questionPoster)
        showPosterImage()
    }

    private fun showPosterImage() {
        binding().questionPoster.show()
        binding().addCoverImageBlock.hide()
    }

    private fun showFragmentChoicePointDialog() = FragmentChoicePointDialog.newInstance(
        point = point,
        setPointChoiceListener = ::handleChoicePoint
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showFragmentChooseQuestionPoster() = FragmentChooseQuestionPoster.newInstance(
        onNetworkPosterSelectedListener = ::onNetworkPosterSelectedListener,
        onLocalPosterSelectedListener = ::onLocalPosterSelectedListener
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showFragmentInputAnswerDialog(
        currentAnswer: String,
        answerIsCorrect: Boolean,
        answerVariantsType: AnswerVariants
    ) = FragmentInputAnswerDialog.newInstance(
        answerVariant = currentAnswer,
        answerIsCorrect = answerIsCorrect,
        answerVariantsType = answerVariantsType,
        setOnConfirmClickListener = ::handleTypedAnswer
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)


    private fun showFragmentChooseTimeDialog() = FragmentChooseTimeDialog.newInstance(
        checkedMinutes = minutes,
        checkedSeconds = seconds,
        setTimeChoiceListener = ::handleChoiceTime
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)
}