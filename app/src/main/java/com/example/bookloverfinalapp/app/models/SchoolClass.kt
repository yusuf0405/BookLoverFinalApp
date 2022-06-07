package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolClass(
    var id: String,
    val title: String,
    val schoolId: String,
) : Parcelable
