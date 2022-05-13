package com.example.data.repository

import com.example.data.cache.models.StudentDb
import com.example.data.cache.source.UsersCacheDataSource
import com.example.data.cloud.models.UserCloud
import com.example.data.cloud.source.UsersCloudDataSource
import com.example.data.models.StudentData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val cloudDataSource: UsersCloudDataSource,
    private val cacheDataSource: UsersCacheDataSource,
    private val studentDomainMapper: Mapper<StudentData, StudentDomain>,
    private val userCloudMapper: Mapper<UserCloud, UserDomain>,
    private val studentDbMapper: Mapper<StudentDb, StudentData>,
) : UserRepository {

    override fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>> = flow {
        emit(Resource.loading())
        emit(cloudDataSource.updateUser(id = id, user = user, sessionToken = sessionToken))
    }

    override fun deleteUser(id: String, sessionToken: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteUser(id = id, sessionToken = sessionToken)
        if (result.status == Status.SUCCESS) {
            cacheDataSource.deleteUser(id = id)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message))

    }

    override fun fetchMyStudents(classId: String): Flow<Resource<List<StudentDomain>>> = flow {
        emit(Resource.loading())
        val userCacheList = cacheDataSource.fetchMyStudents(classId = classId)
        if (userCacheList.isEmpty()) {
            val result = cloudDataSource.fetchMyStudents(classId = classId)
            if (result.status == Status.SUCCESS) {
                val userDataList = result.data!!
                if (userDataList.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveStudents(userDataList)
                    val userDomainList =
                        userDataList.map { userData -> studentDomainMapper.map(userData) }
                    emit(Resource.success(data = userDomainList))
                }
            } else emit(Resource.error(message = result.message!!))
        } else {
            val userDataList = userCacheList.map { userDb -> studentDbMapper.map(userDb) }
            val userDomainList = userDataList.map { userData -> studentDomainMapper.map(userData) }
            if (userDomainList.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = userDomainList))
        }
    }

    override fun fetchClassStudents(classId: String): Flow<Resource<List<StudentDomain>>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.fetchMyStudents(classId = classId)
        if (result.status == Status.SUCCESS) {
            val userDataList = result.data!!
            if (userDataList.isEmpty()) emit(Resource.empty())
            else {
                val userDomainList =
                    userDataList.map { userData -> studentDomainMapper.map(userData) }
                emit(Resource.success(data = userDomainList))
            }
        } else emit(Resource.error(message = result.message!!))
    }


    override fun addSessionToken(id: String, sessionToken: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addSessionToken(id = id, sessionToken = sessionToken)
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))
    }

    override fun getCurrentUser(sessionToken: String): Flow<Resource<UserDomain>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.getCurrentUser(sessionToken = sessionToken)
        when (result.status) {
            Status.SUCCESS -> emit(Resource.success(data = userCloudMapper.map(result.data!!)))
            Status.EMPTY -> emit(Resource.empty())
            Status.ERROR -> emit(Resource.error(message = result.message))
        }
    }


    override suspend fun clearStudentsCache() = cacheDataSource.clearStudents()

}