package com.joseph.profile.domain.models

import android.os.Parcelable
import com.joseph.ui_core.extensions.firstLetterCapital
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFeatureModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val email: String,
    val password: String,
) : Parcelable {

    fun firstNameLetterCapital() = firstName.firstLetterCapital()

    fun lastNameLetterCapital() = lastName.firstLetterCapital()

    fun fullName() = "$firstName $lastName"

    companion object {
        fun unknown() = UserFeatureModel(
            id = String(),
            firstName = "Joseph",
            lastName = "Barbera",
            imageUrl = "https://i.pinimg.com/originals/9a/13/99/9a13990bf715d7b5f5ea51913b6ce1b9.png",
            email = "planetapluton888@gmail.com",
            password = "Joseph123"
        )
    }

}