package com.joseph.profile.domain.models

import android.os.Parcelable
import com.joseph.ui.core.extensions.firstLetterCapital
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
            imageUrl = "https://cdn3.iconfinder.com/data/icons/avatars-9/145/Avatar_Alien-1024.png",
            email = "planetapluton888@gmail.com",
            password = "Joseph123"
        )
    }

}