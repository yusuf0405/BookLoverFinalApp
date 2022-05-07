package com.example.domain.domain.repository

import com.example.domain.models.Resource
import com.example.domain.models.student.PostRequestAnswerDomain
import com.example.domain.domain.models.UserDomain
import com.example.domain.models.student.UserSignUpDomain
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun signIn(email: String, password: String): Flow<Resource<UserDomain>>

    fun signUp(user: UserSignUpDomain): Flow<Resource<PostRequestAnswerDomain>>
}