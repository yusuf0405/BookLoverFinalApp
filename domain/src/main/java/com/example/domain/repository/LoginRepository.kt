package com.example.domain.repository

import com.example.domain.models.UserDomain
import com.example.domain.Resource
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.UserSignUpDomain
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    fun signIn(email: String, password: String): Flow<Resource<UserDomain>>

    fun signUp(user: UserSignUpDomain): Flow<Resource<PostRequestAnswerDomain>>

    fun passwordReset(email: String): Flow<Resource<Unit>>
}