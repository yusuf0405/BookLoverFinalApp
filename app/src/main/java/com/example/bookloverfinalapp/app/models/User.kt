package com.example.bookloverfinalapp.app.models

import android.widget.ImageView
import com.joseph.ui.core.R
import com.joseph.core.extensions.showRoundedImage
import java.util.*

data class User(
    val id: String,
    var createAt: Date,
    var classId: String,
    var schoolId: String,
    var image: UserImage? = null,
    var email: String,
    var schoolName: String,
    var className: String,
    var gender: UserGender,
    var lastname: String,
    var name: String,
    var number: String,
    var password: String? = null,
    var userType: UserType,
    var sessionToken: String,
) {

    fun fullName(): String = "$name $lastname"

    fun showCoverImage(imageView: ImageView) {
        imageView.context.showRoundedImage(
            roundedSize = 100,
            imageUrl = image?.url ?: String(),
            imageView = imageView,
            placeHolder = R.drawable.placeholder_avatar
        )
    }

    companion object {
        fun unknown() = User(
            id = UUID.randomUUID().toString(),
            createAt = Date(),
            classId = String(),
            schoolId = String(),
            image = UserImage(String(), String(), String()),
            email = String(),
            schoolName = String(),
            className = String(),
            gender = UserGender.female,
            lastname = String(),
            name = String(),
            number = String(),
            password = String(),
            userType = UserType.unknown,
            sessionToken = String(),
        )
    }
}

data class UserImage(
    var name: String,
    var type: String,
    var url: String,
)

enum class UserGender {
    male,
    female
}

enum class UserType {
    unknown,
    student,
    teacher,
    admin
}