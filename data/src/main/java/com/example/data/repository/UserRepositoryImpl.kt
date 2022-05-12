package com.example.data.repository

import com.example.data.cache.models.StudentDb
import com.example.data.cache.source.UsersCacheDataSource
import com.example.data.cloud.source.UsersCloudDataSource
import com.example.data.models.StudentData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.StudentDomain
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserRepository
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

    override fun fetchMyStudents(classId: String): Flow<Resource<List<StudentDomain>>> = flow {
        emit(Resource.loading())
        val userCacheList = cacheDataSource.fetchMyStudents(classId = classId)
        if (userCacheList.isEmpty()) {
            val result =
                cloudDataSource.fetchMyStudents(classId = classId)
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