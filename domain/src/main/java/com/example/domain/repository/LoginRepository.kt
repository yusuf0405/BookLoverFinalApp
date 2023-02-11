package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.models.UserDomain
import com.example.domain.Resource
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.UserSignUpDomain
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun signIn(email: String, password: String): RequestState<UserDomain>

    suspend fun signUp(user: UserSignUpDomain): RequestState<PostRequestAnswerDomain>

    suspend fun passwordReset(email: String): RequestState<Unit>
}