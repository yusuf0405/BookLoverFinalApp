package com.example.data.data.repository

import android.util.Log
import com.example.data.api.KnigolyubApi
import com.example.data.data.base.BaseApiResponse
import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.source.UsersCacheDataSource
import com.example.data.data.cloud.source.UsersCloudDataSource
import com.example.data.data.models.StudentData
import com.example.data.toDtoStudent
import com.example.data.toDtoUpdate
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentDomain
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.student.StudentUpdateRes
import com.example.domain.models.student.UpdateAnswer
import com.example.domain.domain.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val api: KnigolyubApi,
    private val cloudDataSource: UsersCloudDataSource,
    private val cacheDataSource: UsersCacheDataSource,
    private val userDomainMapper: Mapper<StudentData, StudentDomain>,
    private val userDbMapper: Mapper<StudentDb, StudentData>,
) : UserRepository, BaseApiResponse() {

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

    override fun fetchMyStudents(
        className: String,
        schoolName: String,
    ): Flow<Resource<List<StudentDomain>>> = flow {
        emit(Resource.loading())
        val userCacheList = cacheDataSource.fetchMyStudents()
        if (userCacheList.isEmpty()) {
            val result =
                cloudDataSource.fetchMyStudents(className = className, schoolName = schoolName)
            if (result.status == Status.SUCCESS) {
                val userDataList = result.data!!
                cacheDataSource.saveUsers(userDataList)
                val userDomainList = userDataList.map { userData -> userDomainMapper.map(userData) }
                emit(Resource.success(data = userDomainList))
            } else emit(Resource.error(message = result.message!!))
        } else {
            val userDataList = userCacheList.map { userDb -> userDbMapper.map(userDb) }
            val userDomainList = userDataList.map { userData -> userDomainMapper.map(userData) }
            emit(Resource.success(data = userDomainList))
        }
    }

}