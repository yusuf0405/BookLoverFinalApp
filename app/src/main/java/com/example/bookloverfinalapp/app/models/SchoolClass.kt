package com.example.bookloverfinalapp.app.models

import java.io.Serializable

data class SchoolClass(
    var id: String,
    val title: String,
    val schoolId: String,
) : Serializable
