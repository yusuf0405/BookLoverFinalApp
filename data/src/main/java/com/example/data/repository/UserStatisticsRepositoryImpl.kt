package com.example.data.repository

import com.example.data.cache.db.UserStatisticDao
import com.example.data.cache.models.UserStatisticCache
import com.example.domain.models.UserStatisticModel
import com.example.domain.repository.UserStatisticsRepository
import java.util.*
import javax.inject.Inject

class UserStatisticsRepositoryImpl @Inject constructor(
    private val dao: UserStatisticDao
) : UserStatisticsRepository {

    override suspend fun fetchStatisticDays(): List<UserStatisticModel> =
        dao.fetchAllStatisticDays().map {
            UserStatisticModel(progress = it.progress, day = it.day)
        }

    override suspend fun saveNewDayToStatistics(progress: Int) {
        val userStatisticDays = dao.fetchAllStatisticDays()
        val calendar: Calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val newDayStatistics = UserStatisticCache(
            day = currentDay,
            progress = progress
        )
        if (userStatisticDays.isNotEmpty()) {
            val lastItem = userStatisticDays.last()
            if (lastItem.day == currentDay) {
                lastItem.progress += progress
                userStatisticDays[userStatisticDays.lastIndex] = lastItem
            } else userStatisticDays.add(newDayStatistics)
        } else userStatisticDays.add(newDayStatistics)

        if (userStatisticDays.size > MAX_DAYS_COUNT) userStatisticDays.removeAt(0)
        dao.saveNewStatistics(userStatisticDays)
    }

    private companion object {
        const val MAX_DAYS_COUNT = 7
    }
}