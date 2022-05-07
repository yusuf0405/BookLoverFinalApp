package com.example.domain.models.student

data class UserSignUpDomain(
    val name: String,
    val lastname: String,
    val email: String,
    val password: String,
    val number: String,
    val className: String,
    val schoolName: String,
    val gender: String,
    val classId: String,
    val userType: String,
)
