package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class SignUpAnswerCloud(
    @SerializedName("objectId") val objectId: String,
    @SerializedName("image") val image: UserImageCloud,
    @SerializedName("sessionToken") val sessionToken: String,
    @SerializedName("createdAt") val createdAt: Date,
) {
    companion object {

        fun unknown() = SignUpAnswerCloud(
            objectId = String(),
            sessionToken = String(),
            createdAt = Date(),
            image = UserImageCloud.unknown(),
        )
    }
}
