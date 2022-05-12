package com.example.data.repository

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.models.PasswordResetCloud
import com.example.data.cloud.models.SignUpAnswerCloud
import com.example.data.cloud.models.UserCloud
import com.example.data.cloud.service.LoginService
import com.example.data.toDtoSignUp
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.repository.LoginRepository
import com.example.domain.Resource
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.UserSignUpDomain
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
            service.signIn(session = 1, username = email, password = password)
        })
    }

    override fun signUp(user: UserSignUpDomain): Flow<Resource<PostRequestAnswerDomain>> = flow {
        emit(Resource.loading())
        emit(safeApiMapperCall(mapper = signUpMapper) {
            service.signUp(session = 1,
                user = user.toDtoSignUp())
        })
    }

    override fun passwordReset(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        emit(safeApiCall { service.requestPasswordReset(PasswordResetCloud(email = email)) })
    }
}