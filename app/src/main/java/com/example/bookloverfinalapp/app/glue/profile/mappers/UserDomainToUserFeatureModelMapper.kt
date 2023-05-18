package com.example.bookloverfinalapp.app.glue.profile.mappers

import com.example.domain.models.UserDomain
import com.joseph.common_api.Mapper
import com.joseph.profile.domain.models.UserFeatureModel
import javax.inject.Inject

class UserDomainToUserFeatureModelMapper @Inject constructor() :
    Mapper<UserDomain, UserFeatureModel> {

    override fun map(from: UserDomain): UserFeatureModel = from.run {
        UserFeatureModel(
            id = id,
            firstName = name,
            lastName = lastname,
            imageUrl = image.url,
            email = email,
            password = password ?: String()
        )
    }
}