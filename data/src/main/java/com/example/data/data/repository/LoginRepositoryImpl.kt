package com.example.data.data.repository

import com.example.data.data.ResourceProvider
import com.example.data.data.base.BaseApiResponse
import com.example.data.data.cloud.models.SignUpAnswerCloud
import com.example.data.data.cloud.models.UserCloud
import com.example.data.data.cloud.service.LoginService
import com.example.data.toDtoSignUp
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.UserDomain
import com.example.domain.domain.repository.LoginRepository
import com.example.domain.models.Resource
import com.example.domain.models.student.PostRequestAnswerDomain
import com.example.domain.models.student.UserSignUpDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginRepositoryImpl(
    private val service: LoginService,
    private val signInMapper: Mapper<UserCloud, UserDomain>,
    private val signUpMapper: Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>,
    resourceProvider: ResourceProvider,
) : LoginRepository, BaseApiResponse(resourceProvider = resourceProvider) {

    override fun signIn(email: String, password: String): Flow<Resource<UserDomain>> = flow {
        emit(Resource.loading())
        emit(safeApiMapperCall(mapper = signInMapper) {
            service.signIn(session = 1, username = email, password = password) })
    }

    override fun signUp(user: UserSignUpDomain): Flow<Resource<PostRequestAnswerDomain>> = flow {
        emit(Resource.loading())
        emit(safeApiMapperCall(mapper = signUpMapper) { service.signUp(session = 1, user = user.toDtoSignUp()) })
    }
}