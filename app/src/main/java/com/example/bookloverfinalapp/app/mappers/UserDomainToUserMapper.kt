package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserGender
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.models.UserType
import com.example.domain.Mapper
import com.example.domain.models.UserDomain

class UserDomainToUserMapper : Mapper<UserDomain, User> {
    override fun map(from: UserDomain): User = from.run {
        if (from == UserDomain.unknown()) User.unknown()
        else User(
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
            image = UserImage(name = image.name, url = image.url, type = image.type),
            userType = userType(userType),
            sessionToken = sessionToken,
            schoolId = schoolId
        )
    }

    private fun userGender(gender: String) =
        if (gender == "female") UserGender.female
        else UserGender.male

    private fun userType(userType: String): UserType =
        when (userType) {
            "student" -> UserType.student
            "teacher" -> UserType.teacher
            "admin" -> UserType.admin
            else -> UserType.unknown
        }
}