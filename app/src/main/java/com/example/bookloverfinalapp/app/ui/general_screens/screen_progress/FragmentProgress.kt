package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R as UiCore
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserRatingFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserTopRatingFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.adapter.DayOfTheWeekAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.hide
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.databinding.FragmentProgressBinding
import com.example.domain.models.UserStatisticModel
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.statistics.library.line_chart.data.DataEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import java.util.Calendar

@AndroidEntryPoint
class FragmentProgress :
    BaseFragment<FragmentProgressBinding, FragmentProgressViewModel>(FragmentProgressBinding::inflate) {

    override val viewModel: FragmentProgressViewModel by viewModels()

    private val statisticsAdapter = FingerprintAdapter(
        listOf(
            DayOfTheWeekFingerprint()
        )
    )
    private val leaderboardAdapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            UserTopRatingFingerprint(),
            UserRatingFingerprint()
        )
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isHaveToolbar = true
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()

        binding().analyticalPieChart.setDataChart(
            listOf(
                Pair(4, "my books"),
                Pair(6, "joint books"),
                Pair(6, "books supported"),
            )
        )
        binding().analyticalPieChart.startAnimation()

    }

    private fun setupViews() = with(binding()) {
        includeDefaultToolbar.title.text = getString(com.joseph.ui.core.R.string.my_statistics)
        includeDefaultToolbar.toolbar.navigationIcon = null
        dayOfTheWeekRecyclerView.adapter = statisticsAdapter
        leaderboardRecyclerView.adapter = leaderboardAdapter
        dayOfTheWeekRecyclerView.itemAnimator = createAddableItemAnimator()
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            fetchUserStatisticDays()
            userStatisticDays.observe(::handleStatisticDaysFetching)
            studentsFromClass.filter { it.isNotEmpty() }.observe(leaderboardAdapter::submitList)
//            userAndProgressFlow.filterNotNull().observe(::handleCurrentUserFetching)
        }
    }

    private fun setOnClickListeners() = with(binding()) {
        readBookButton.setOnDownEffectClickListener {
            viewModel.navigateToAllSavedBooksFragment()
        }
    }

    private fun handleCurrentUserFetching(user: Student) = with(binding()) {
        progressBlock.booksCount.text = user.booksRead.toString()
        progressBlock.pagesCount.text = user.progress.toString()
        progressBlock.chapterCount.text = user.chaptersRead.toString()
    }

    private fun handleStatisticDaysFetching(statisticDays: List<UserStatisticModel>) {
        if (statisticDays.size <= 3) {
            binding().statisticsAnimationContainer.show()
            binding().statisticsContainer.hide()
            return
        }
        binding().statisticsAnimationContainer.hide()
        binding().statisticsContainer.show()
        val dataEntity = statisticDays.mapIndexed { index, value ->
            val entity = DataEntity(index)
            entity.value = value.progress
            entity.des = checkDayAndReturnDayShortTitle(value.day)
            entity
        }
        val progressOfTheWeek = statisticDays.map { it.progress }
        var progress = 0
        progressOfTheWeek.map { progress += it }
        val dayOfTheWeekAdapterModels = statisticDays.map {
            DayOfTheWeekAdapterModel(
                day = checkDayAndReturnDayLongTitle(it.day),
                progress = it.progress
            )
        }.toMutableList()

        dayOfTheWeekAdapterModels.add(
            DayOfTheWeekAdapterModel(
                day = getString(UiCore.string.your_point_this_week),
                progress = progress
            )
        )
        statisticsAdapter.submitList(dayOfTheWeekAdapterModels.toList())
        if (dataEntity.size > 1) updateStatisticsView(dataEntity)
    }

    private fun createAddableItemAnimator() =
        AddableItemAnimator(SimpleCommonAnimator()).also { anim ->
            anim.addViewTypeAnimation(
                R.layout.item_day_of_the_week,
                SlideInLeftCommonAnimator()
            )
            anim.addDuration = 700L
            anim.removeDuration = 700L
        }

    private fun updateStatisticsView(dataEntity: List<DataEntity>) = with(binding().lineChartView) {
        invalidate()
        requestLayout()
        verticalParts = 7
        setData(dataEntity)
        startAnim()
    }

    private fun checkDayAndReturnDayShortTitle(day: Int): String =
        when (day) {
            Calendar.MONDAY -> getString(UiCore.string.mon)
            Calendar.TUESDAY -> getString(UiCore.string.tue)
            Calendar.THURSDAY -> getString(UiCore.string.thu)
            Calendar.SATURDAY -> getString(UiCore.string.sat)
            Calendar.SUNDAY -> getString(UiCore.string.sun)
            Calendar.WEDNESDAY -> getString(UiCore.string.web)
            Calendar.FRIDAY -> getString(UiCore.string.fri)
            else -> getString(UiCore.string.progress)
        }

    private fun checkDayAndReturnDayLongTitle(day: Int): String =
        when (day) {
            Calendar.MONDAY -> getString(UiCore.string.monday)
            Calendar.TUESDAY -> getString(UiCore.string.tuesday)
            Calendar.THURSDAY -> getString(UiCore.string.thursday)
            Calendar.SATURDAY -> getString(UiCore.string.satuday)
            Calendar.SUNDAY -> getString(UiCore.string.sunday)
            Calendar.WEDNESDAY -> getString(UiCore.string.webnesday)
            Calendar.FRIDAY -> getString(UiCore.string.friday)
            else -> getString(UiCore.string.progress)
        }
}