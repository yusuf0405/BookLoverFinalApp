package com.example.bookloverfinalapp.app.ui.mappers

import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.UserDomain

class UserDomainToUserMapper : Mapper<UserDomain, User>() {
    override fun map(from: UserDomain): User = from.run {
        User(
            createAt = createAt,
            classId = classId,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            number = number,
            schoolName = schoolName,
            className = className,
            id = id,
            image = image?.let { UserImage(name = it.name, url = it.url, type = it.type) },
            userType = userType(userType),
            sessionToken = sessionToken
        )
    }

    fun userType(userType: String): UserType =
        when (userType) {
            "student" -> UserType.student
            "teacher" -> UserType.teacher
            else -> UserType.admin
        }
}