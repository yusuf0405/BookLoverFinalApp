package com.joseph.profile.domain.usecases

import com.joseph.common_api.CheckInternetConnection
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.repositories.UserInfoRepository
import com.joseph.utils_core.extensions.addDays
import com.joseph.utils_core.extensions.startOfDay
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

interface FetchUserInfoUseCase {

    suspend operator fun invoke(): UserFeatureModel

}

class FetchUserInfoUseCaseImpl @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
    private val checkInternetConnection: CheckInternetConnection
) : FetchUserInfoUseCase {

    override suspend fun invoke(): UserFeatureModel {
        return if (checkInternetConnection.isOnline()) userInfoRepository.fetchUserInfoFromCloud()
        else userInfoRepository.fetchUserInfoFromCache()
    }

    private fun checkLastSaveDate(lastSaveDate: Date): Boolean =
        lastSaveDate.startOfDay() <= getCurrentDate().startOfDay()

    private fun getCurrentDate() = Calendar.getInstance().time

    private fun addDayToCurrentDate() = getCurrentDate().addDays(1)
}