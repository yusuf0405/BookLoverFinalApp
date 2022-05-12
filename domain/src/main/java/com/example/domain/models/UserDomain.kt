package com.example.domain.models

import java.util.*


data class UserDomain(
    val id: String,
    var createAt: Date,
    var classId: String,
    var schoolId: String,
    var image: UserDomainImage? = null,
    var email: String,
    var schoolName: String,
    var className: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var password: String? = null,
    var userType: String,
    var sessionToken: String,
)

data class UserDomainImage(
    var name: String,
    var type: String,
    var url: String,
)