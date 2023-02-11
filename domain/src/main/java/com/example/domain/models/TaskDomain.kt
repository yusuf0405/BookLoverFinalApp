package com.example.domain.models

import java.util.Date


data class TaskDomain(
    val id: String,
    val title: String,
    val description: String,
    val classId: String,
    val startDate: Date,
    val endDate: Date,
    val taskGenres: List<String>,
)