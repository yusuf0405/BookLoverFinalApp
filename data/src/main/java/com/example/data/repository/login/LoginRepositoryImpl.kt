package com.example.data.repository.login

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.models.PasswordResetCloud
import com.example.data.cloud.models.SignUpAnswerCloud
import com.example.data.cloud.models.UserCloud
import com.example.data.cloud.service.LoginService
import com.example.data.repository.BaseRepository
import com.example.data.toDtoSignUp
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.models.PostRequestAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserSignUpDomain
import com.example.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val service: LoginService,
    private val signInMapper: Mapper<UserCloud, UserDomain>,
    private val signUpMapper: Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>,
    resourceProvider: ResourceProvider,
) : LoginRepository, BaseApiResponse(resourceProvider = resourceProvider), BaseRepository {

    override suspend fun signIn(email: String, password: String): RequestState<UserDomain> {
        val result = safeApiMapperCalll(mapper = signInMapper) {
            service.signIn(session = 1, username = email, password = password)
        }
        return renderResult(
            result = result,
        )
    }

    override suspend fun signUp(user: UserSignUpDomain): RequestState<PostRequestAnswerDomain> {
        val result = safeApiCalll { service.signUp(session = 1, user = user.toDtoSignUp()) }
        return renderResult(result = result).map(signUpMapper)
    }

    override suspend fun passwordReset(email: String): RequestState<Unit> {
        val result = safeApiCalll { service.requestPasswordReset(PasswordResetCloud(email)) }
        return renderResult(result = result)
    }
}