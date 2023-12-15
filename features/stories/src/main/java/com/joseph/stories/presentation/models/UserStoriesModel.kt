package com.joseph.stories.presentation.models

import android.os.Parcelable
import com.joseph.ui.core.adapter.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserStoriesModel(
    val userId: String,
    val name: String,
    val previewImageUrl: String,
    val stories: List<StoriesModel>
) : Parcelable, Item
