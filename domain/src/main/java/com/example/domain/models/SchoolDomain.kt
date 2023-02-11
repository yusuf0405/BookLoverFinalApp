package com.example.domain.models

data class SchoolDomain(
    var id: String,
    var title: String,
) {
    companion object {
        val UNKNOWN = SchoolDomain(
            id = String(),
            title = String()
        )
    }
}