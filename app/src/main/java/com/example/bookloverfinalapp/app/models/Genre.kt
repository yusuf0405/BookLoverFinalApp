package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Genre(
    val id: String,
    val titles: List<String>,
    val descriptions: List<String>,
    val poster: GenrePoster
) : Parcelable

@Parcelize
data class GenrePoster(
    var name: String,
    var url: String,
) : Parcelable