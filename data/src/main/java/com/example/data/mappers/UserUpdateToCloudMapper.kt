package com.example.data.mappers

import com.example.data.cloud.models.UserImageCloud
import com.example.data.cloud.models.UserUpdateCloud
import com.example.domain.Mapper
import com.example.domain.models.UserDomainImage
import com.example.domain.models.UserUpdateDomain

class UserUpdateToCloudMapper : Mapper<UserUpdateDomain, UserUpdateCloud> {
    override fun map(from: UserUpdateDomain): UserUpdateCloud = from.run {
        UserUpdateCloud(image = image.toDtoImage(),
            lastname = lastname,
            name = name,
            gender = gender,
            email = email,
            number = number,
            username = email)
    }

    private fun UserDomainImage.toDtoImage(): UserImageCloud =
        UserImageCloud(
            name = name,
            url = url,
            type = type
        )
}