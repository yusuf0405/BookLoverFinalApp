package com.example.data.data.repository

import com.example.data.api.KnigolyubApi
import com.example.data.data.base.BaseApiResponse
import com.example.data.toDtoStudent
import com.example.data.toDtoUpdate
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.student.StudentUpdateRes
import com.example.domain.models.student.UpdateAnswer
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val api: KnigolyubApi) : UserRepository, BaseApiResponse() {

    override fun updateUser(
        id: String, student: StudentUpdateRes,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswer>> = flow {
        emit(Resource.loading())
        val result = safeApiCall {
            api.updateUser(id = id, student = student.toDtoStudent(), sessionToken = sessionToken)
        }
        when (result.status) {
            Status.SUCCESS -> emit(Resource.success(data = result.data!!.toDtoUpdate()))
            Status.ERROR -> emit(Resource.error(message = result.message))
            Status.NETWORK_ERROR -> emit(Resource.networkError())
            Status.LOADING -> TODO()
        }
    }
}