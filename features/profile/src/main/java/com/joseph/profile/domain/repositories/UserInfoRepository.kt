package com.joseph.profile.domain.repositories

import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.models.UserUpdateModel

interface UserInfoRepository {

    suspend fun fetchUserInfoFromCloud(): UserFeatureModel

    suspend fun fetchUserInfoFromCache(): UserFeatureModel

    suspend fun updateUserProfile(userUpdateModel: UserUpdateModel)
}