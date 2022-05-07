package com.example.domain.models.school

data class School(
    var objectId: String,
    var title: String,
    var classesIds:  List<String>,
)