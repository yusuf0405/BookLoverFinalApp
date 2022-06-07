package com.example.data.cloud.mappers

import com.example.data.cloud.models.UserCloud
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.models.UserDomainImage

class UserCloudToUserDomainMapper : Mapper<UserCloud, UserDomain> {
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
            sessionToken = sessionToken,
            schoolId = schoolId)
    }
}