package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserGender
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.models.UserType
import com.example.data.models.UserImageData
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.models.UserDomainImage

class UserToUserDomainMapper : Mapper<User, UserDomain> {
    override fun map(from: User): UserDomain = from.run {
        if (from == User.unknown()) UserDomain.unknown()
        else UserDomain(
            createAt = createAt,
            classId = classId,
            email = email,
            gender = userGender(gender),
            lastname = lastname,
            name = name,
            number = number,
            schoolName = schoolName,
            className = className,
            id = id,
            image = image?.let { UserDomainImage(name = it.name, url = it.url, type = it.type) }
                ?: UserDomainImage.unknown(),
            userType = userType.name,
            sessionToken = sessionToken,
            schoolId = schoolId
        )
    }


    private fun userGender(gender: UserGender) =
        if (gender == UserGender.female) UserGender.female.name
        else UserGender.male.name
}