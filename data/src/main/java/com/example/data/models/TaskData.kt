package com.example.data.models

import java.util.*


data class TaskData(
    val id: String,
    val title: String,
    val description: String,
    val classId: String,
    val startDate: Date,
    val endDate: Date,
    val taskGenres: List<String>,
)