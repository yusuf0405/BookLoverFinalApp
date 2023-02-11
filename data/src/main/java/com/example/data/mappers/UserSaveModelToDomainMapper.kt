package com.example.data.mappers

import com.example.data.models.UserSaveModel
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.models.UserDomainImage

class UserSaveModelToDomainMapper : Mapper<UserSaveModel, UserDomain> {
    override fun map(from: UserSaveModel): UserDomain = from.run {
        if (from == UserSaveModel.unknown()) {
            UserDomain.unknown()
        } else UserDomain(
            createAt = createAt,
            classId = classId,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            image = UserDomainImage(name = image.name, type = image.type, url = image.url),
            number = number,
            schoolName = schoolName,
            className = className,
            id = id,
            userType = userType,
            sessionToken = sessionToken,
            schoolId = schoolId
        )
    }
}