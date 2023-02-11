package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import com.example.bookloverfinalapp.app.utils.extensions.toDto
import com.example.domain.models.UserDomainImage
import com.example.domain.models.UserSignUpDomain
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class UserSignUp(
    val name: String = String(),
    val lastname: String = String(),
    var email: String = String(),
    var password: String = String(),
    val number: String = String(),
    var className: String = String(),
    var schoolName: String = String(),
    val gender: String = String(),
    var classId: String = String(),
    var schoolId: String = String(),
    val userType: String = String(),
) : Parcelable {

    fun mapToUserSignUpDomain() = UserSignUpDomain(
        name = name,
        lastname = lastname,
        email = email,
        password = password,
        number = number,
        className = className,
        schoolName = schoolName,
        gender = gender,
        classId = classId,
        schoolId = schoolId,
        userType = userType
    )

    fun mapToUser(
        id: String,
        createdAt: Date,
        sessionToken: String,
        image: UserDomainImage,
    ) = User(
        name = name,
        lastname = lastname,
        email = email,
        password = password,
        number = number,
        className = className,
        schoolName = schoolName,
        gender = userGender(gender),
        classId = classId,
        schoolId = schoolId,
        createAt = createdAt,
        id = id,
        image = image.toDto(),
        sessionToken = sessionToken,
        userType = UserType.valueOf(userType)
    )

    private fun userGender(gender: String) =
        if (gender == "female") UserGender.female
        else UserGender.male
}
