package com.example.data.data.repository

import com.example.data.api.KnigolyubApi
import com.example.data.data.base.BaseApiResponse
import com.example.data.toDtoSignUp
import com.example.data.toRequestAnswer
import com.example.data.toUser
import com.example.domain.models.Resource
import com.example.domain.models.Status.*
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.models.student.UserDomain
import com.example.domain.models.student.UserSignUpRes
import com.example.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginRepositoryImpl(private val api: KnigolyubApi) : LoginRepository, BaseApiResponse() {
    override fun signIn(email: String, password: String): Flow<Resource<UserDomain>> = flow {
        emit(Resource.loading())
        val result = safeApiCall { api.signIn(session = 1, username = email, password = password) }
        when (result.status) {
            SUCCESS -> emit(Resource.success(data = result.data!!.toUser()))
            ERROR -> emit(Resource.error(message = result.message))
            NETWORK_ERROR -> emit(Resource.networkError())
            LOADING -> TODO()
        }
    }


    override fun signUp(user: UserSignUpRes): Flow<Resource<PostRequestAnswer>> = flow {
        emit(Resource.loading())
        val result = safeApiCall { api.signUp(session = 1, user = user.toDtoSignUp()) }
        when (result.status) {
            SUCCESS -> emit(Resource.success(data = result.data!!.toRequestAnswer()))
            ERROR -> emit(Resource.error(message = result.message))
            NETWORK_ERROR -> emit(Resource.networkError())
            LOADING -> TODO()
        }
    }
}