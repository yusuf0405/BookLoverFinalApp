package com.example.domain.repository

import com.example.domain.models.UserStatisticModel

interface UserStatisticsRepository {

    suspend fun fetchStatisticDays(): List<UserStatisticModel>

    suspend fun saveNewDayToStatistics(progress: Int)

}