package com.example.bookloverfinalapp.app.glue.profile

import com.example.domain.models.UserDomain
import com.example.domain.models.UserDomainImage
import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import com.joseph.common_api.Mapper
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.models.UserUpdateModel
import com.joseph.profile.domain.repositories.UserInfoRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AdapterUserInfoRepository @Inject constructor(
    private val userRepository: UserRepository,
    private val userCacheRepository: UserCacheRepository,
    private val userDomainToUserFeatureModelMapper: Mapper<UserDomain, UserFeatureModel>
) : UserInfoRepository {

    override suspend fun fetchUserInfoFromCloud(): UserFeatureModel {
        val user = userCacheRepository.fetchCurrentUserFromCache()
        return userRepository.fetchUserInfoFromCloud(sessionToken = user.sessionToken)
            .onEach(userCacheRepository::saveCurrentUserFromCache)
            .map(userDomainToUserFeatureModelMapper::map)
            .firstOrNull() ?: UserFeatureModel.unknown()
    }

    override suspend fun fetchUserInfoFromCache(): UserFeatureModel =
        userCacheRepository.fetchCurrentUserFromCacheFlow()
            .map(userDomainToUserFeatureModelMapper::map)
            .firstOrNull() ?: UserFeatureModel.unknown()

    override suspend fun updateUserProfile(userUpdateModel: UserUpdateModel) {
//        userRepository.updateUserParameters(
//            id = userUpdateModel.id,
//            sessionToken = userUpdateModel.sessionToken,
//            user = UserUpdateDomain(
//                UserDomainImage.unknown()
//            )
//        )
    }
}