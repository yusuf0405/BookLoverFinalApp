package com.example.data.data.cloud.mappers

import com.example.data.data.cloud.models.UserCloud
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.UserDomain
import com.example.domain.domain.models.UserDomainImage

class UserCloudToUserDomainMapper : Mapper<UserCloud, UserDomain>() {
    override fun map(from: UserCloud): UserDomain = from.run {
        UserDomain(createAt = createAt,
            classId = classId,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            image = UserDomainImage(name = image.name, type = image.type, url = image.url),
            number = number,
            schoolName = schoolName,
            className = className,
            id = objectId,
            userType = userType,
            sessionToken = sessionToken)
    }
}