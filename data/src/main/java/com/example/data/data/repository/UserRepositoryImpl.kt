package com.example.data.data.repository

import com.example.data.data.cache.models.StudentDb
import com.example.data.data.cache.source.UsersCacheDataSource
import com.example.data.data.cloud.source.UsersCloudDataSource
import com.example.data.data.models.StudentData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.StudentDomain
import com.example.domain.domain.repository.UserRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.student.UpdateAnswerDomain
import com.example.domain.models.student.UserUpdateDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(
    private val cloudDataSource: UsersCloudDataSource,
    private val cacheDataSource: UsersCacheDataSource,
    private val userDomainMapper: Mapper<StudentData, StudentDomain>,
    private val userDbMapper: Mapper<StudentDb, StudentData>,
) : UserRepository {

    override fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ): Flow<Resource<UpdateAnswerDomain>> = flow {
        emit(Resource.loading())
        emit(cloudDataSource.updateUser(id = id, user = user, sessionToken = sessionToken))
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
                if (userDataList.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveStudents(userDataList)
                    val userDomainList =
                        userDataList.map { userData -> userDomainMapper.map(userData) }
                    emit(Resource.success(data = userDomainList))
                }
            } else emit(Resource.error(message = result.message!!))
        } else {
            val userDataList = userCacheList.map { userDb -> userDbMapper.map(userDb) }
            val userDomainList = userDataList.map { userData -> userDomainMapper.map(userData) }
            if (userDomainList.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = userDomainList))
        }
    }

    override suspend fun clearStudentsCache() = cacheDataSource.clearStudents()

}