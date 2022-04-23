package com.example.domain.models.classes


data class Class(
    var objectId: String,
    var teachers: List<String>? = null,
    var title: String,
    var students: List<String>? = null,
)