package com.example.data.mappers

import com.example.data.models.UserSaveModel
import com.example.data.models.UserSaveModelImage
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.models.UserDomainImage

class UserDomainToSaveModelMapper : Mapper<UserDomain, UserSaveModel> {
    override fun map(from: UserDomain): UserSaveModel = from.run {
        UserSaveModel(
            createAt = createAt,
            classId = classId,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            image = UserSaveModelImage(name = image.name, type = image.type, url = image.url),
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