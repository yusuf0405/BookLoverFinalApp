package com.example.domain.repository

import com.example.domain.models.Resource
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.models.student.UserDomain
import com.example.domain.models.student.UserSignUpRes
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun signIn(email: String, password: String): Flow<Resource<UserDomain>>

    fun signUp(user: UserSignUpRes): Flow<Resource<PostRequestAnswer>>
}