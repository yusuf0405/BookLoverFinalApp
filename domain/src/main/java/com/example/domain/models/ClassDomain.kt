package com.example.domain.models


data class ClassDomain(
    var id: String,
    var title: String,
    var schoolId: String,
) {
    companion object {
        fun unknown() = ClassDomain(
            id = String(),
            title = String(),
            schoolId = String()
        )
    }
}