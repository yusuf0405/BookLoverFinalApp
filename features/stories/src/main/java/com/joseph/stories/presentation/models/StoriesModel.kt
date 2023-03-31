package com.joseph.stories.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StoriesModel(
    val storiesId: String,
    val userId: String,
    val schoolId: String,
    val title: String,
    val description: String,
    val imageFileUrl: String,
    var previewImageUrl: String,
    var videoFileUrl: String,
    val mediaType: MediaType,
    val publishedDate: Date,
) : Parcelable


enum class MediaType {
    VIDEO,
    IMAGE
}
