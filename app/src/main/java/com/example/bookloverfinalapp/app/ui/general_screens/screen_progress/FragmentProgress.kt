package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInLeftCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.adapter.DayOfTheWeekAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserRatingFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter.UserTopRatingFingerprint
import com.example.bookloverfinalapp.databinding.FragmentProgressBinding
import com.example.domain.models.UserStatisticModel
import com.joseph.ui_core.extensions.launchWhenStarted
import com.statistics.library.line_chart.data.DataEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import java.util.*

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
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setupViews()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        dayOfTheWeekRecyclerView.adapter = statisticsAdapter
        leaderboardRecyclerView.adapter = leaderboardAdapter
        dayOfTheWeekRecyclerView.itemAnimator = createAddableItemAnimator()
    }

    private fun observeData() = with(viewModel) {
        launchWhenStarted {
            fetchUserStatisticDays()
            userStatisticDays.filter { it.isNotEmpty() }.observe(::handleStatisticDaysFetching)
            studentsFromClass.filter { it.isNotEmpty() }.observe(leaderboardAdapter::submitList)
//            userAndProgressFlow.filterNotNull().observe(::handleCurrentUserFetching)
        }
    }

    private fun handleCurrentUserFetching(user: Student) = with(binding()) {
        progressBlock.booksCount.text = user.booksRead.toString()
        progressBlock.pagesCount.text = user.progress.toString()
        progressBlock.chapterCount.text = user.chaptersRead.toString()
    }

    private fun handleStatisticDaysFetching(statisticDays: List<UserStatisticModel>) {
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
                day = getString(R.string.your_point_this_week),
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
            Calendar.MONDAY -> getString(R.string.mon)
            Calendar.TUESDAY -> getString(R.string.tue)
            Calendar.THURSDAY -> getString(R.string.thu)
            Calendar.SATURDAY -> getString(R.string.sat)
            Calendar.SUNDAY -> getString(R.string.sun)
            Calendar.WEDNESDAY -> getString(R.string.web)
            Calendar.FRIDAY -> getString(R.string.fri)
            else -> getString(R.string.exo_track_unknown)
        }

    private fun checkDayAndReturnDayLongTitle(day: Int): String =
        when (day) {
            Calendar.MONDAY -> getString(R.string.monday)
            Calendar.TUESDAY -> getString(R.string.tuesday)
            Calendar.THURSDAY -> getString(R.string.thursday)
            Calendar.SATURDAY -> getString(R.string.satuday)
            Calendar.SUNDAY -> getString(R.string.sunday)
            Calendar.WEDNESDAY -> getString(R.string.webnesday)
            Calendar.FRIDAY -> getString(R.string.friday)
            else -> getString(R.string.exo_track_unknown)
        }
}