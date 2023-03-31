package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserRatingFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserTopRatingFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.FetchRatingType
import com.joseph.utils_core.extensions.getAttrColor
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentLeaderboardChartBinding
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter


@AndroidEntryPoint
class FragmentLeaderboardChart :
    BaseFragment<FragmentLeaderboardChartBinding, FragmentLeaderboardChartViewModel>(
        FragmentLeaderboardChartBinding::inflate
    ) {

    override val viewModel: FragmentLeaderboardChartViewModel by viewModels()

    private val adapter = FingerprintAdapter(
        listOf(
            UserTopRatingFingerprint(),
            UserRatingFingerprint()
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        hideBottomNavigationView()
        setupViews()
        observeResource()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding()) {
        ratingRecyclerView.adapter = adapter
        ratingRecyclerView.itemAnimator = createAddableItemAnimator()
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            studentsFlow.filter { it.isNotEmpty() }.observe(::populateModels)
            fetchRatingType.observe(::handleFetchType)
        }
    }

    private fun setOnClickListeners() = with(binding()) {
        choiceClassBlock.setOnDownEffectClickListener {
            viewModel.updateFetchRatingType(FetchRatingType.CLASS)
        }
        choiceSchoolBlock.setOnDownEffectClickListener {
            viewModel.updateFetchRatingType(FetchRatingType.SCHOOL)
        }
        upButton.setOnDownEffectClickListener {
            viewModel.navigateBack()
        }
    }

    private fun createAddableItemAnimator() = AddableItemAnimator(SimpleCommonAnimator())
        .also { anim ->
            anim.addViewTypeAnimation(
                R.layout.item_student_rating,
                SlideInLeftCommonAnimator()
            )
            anim.addViewTypeAnimation(
                R.layout.item_top_rating_users,
                SlideInTopCommonAnimator()
            )
            anim.addDuration = ITEMS_ADD_ANIMATOR_DURATION
            anim.removeDuration = ITEMS_REMOVE_ANIMATOR_DURATION
        }

    private fun handleFetchType(type: FetchRatingType) = with(binding()) {
        when (type) {
            FetchRatingType.CLASS -> {
                setTypeToCheckedColor(choiceClass)
                setTypeToUncheckedColor(choiceSchool)
            }
            FetchRatingType.SCHOOL -> {
                setTypeToCheckedColor(choiceSchool)
                setTypeToUncheckedColor(choiceClass)
            }
        }
    }

    private fun populateModels(items: List<Item>) {
        adapter.submitList(items) {
            binding().ratingRecyclerView.scrollToPosition(0)
        }
    }

    private fun setTypeToCheckedColor(textView: TextView) {
        textView.setTextColor(requireContext().getAttrColor(R.attr.blackOrWhiteColor))
    }

    private fun setTypeToUncheckedColor(textView: TextView) {
        textView.setTextColor(Color.GRAY)
    }


    private companion object {
        const val ITEMS_ADD_ANIMATOR_DURATION = 1000L
        const val ITEMS_REMOVE_ANIMATOR_DURATION = 500L
    }
}