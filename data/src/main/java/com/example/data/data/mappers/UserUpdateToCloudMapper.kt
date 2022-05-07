package com.example.data.data.mappers

import com.example.data.data.cloud.models.UserImageCloud
import com.example.data.data.cloud.models.UserUpdateCloud
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.UserDomainImage
import com.example.domain.models.student.UserUpdateDomain

class UserUpdateToCloudMapper : Mapper<UserUpdateDomain, UserUpdateCloud>() {
    override fun map(from: UserUpdateDomain): UserUpdateCloud = from.run {
        UserUpdateCloud(image = image.toDtoImage(),
            lastname = lastname,
            name = name,
            gender = gender,
            email = email,
            number = number)
    }

    private fun UserDomainImage.toDtoImage(): UserImageCloud =
        UserImageCloud(
            name = name,
            url = url,
            type = type
        )
}