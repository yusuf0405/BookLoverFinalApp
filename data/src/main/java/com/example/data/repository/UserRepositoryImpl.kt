package com.example.data.repository

import android.util.Log
import com.example.data.cache.models.UserCache
import com.example.data.cache.source.users.UsersCacheDataSource
import com.example.data.cloud.models.UserCloud
import com.example.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.cloud.source.UsersCloudDataSource
import com.example.data.models.StudentData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.models.StudentDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserRepository
import com.example.domain.repository.UserStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class UserRepositoryImpl(
    private val cloudDataSource: UsersCloudDataSource,
    private val savedBookCloudDataSource: BooksThatReadCloudDataSource,
    private val cacheDataSource: UsersCacheDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val studentDomainMapper: Mapper<StudentData, StudentDomain>,
    private val userCloudToDomainMapper: Mapper<UserCloud, UserDomain>,
    private val userCacheMapper: Mapper<UserCache, StudentData>,
) : UserRepository, BaseRepository {

    override suspend fun updateUserParameters(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ) = renderResult(
        result = cloudDataSource.updateUser(id = id, user = user, sessionToken = sessionToken)
    )

    override suspend fun deleteUser(id: String, sessionToken: String) = renderResultToUnit(
        result = cloudDataSource.deleteUser(id = id, sessionToken = sessionToken),
        onSuccess = { cacheDataSource.deleteUser(id = id) }
    )

    override fun fetchAllStudentsFromClassId(
        classId: String,
        schoolId: String,
        currentUserId: String
    ): Flow<List<StudentDomain>> = flow {
        emit(cacheDataSource.fetchAllStudentsFromClassIdSingle(classId))
    }.flowOn(dispatchersProvider.io())
        .flatMapLatest { cachedUsers ->
            handleCachedClassUsers(
                cachedUsers = cachedUsers,
                classId = classId,
                schoolId = schoolId,
                currentUserId = currentUserId
            )
        }
        .map(::mapUserDataList)
        .flowOn(dispatchersProvider.default())

    override fun fetchAllUsersFromBookId(
        bookId: String,
        schoolId: String,
        currentUserId: String
    ): Flow<List<UserDomain>> = savedBookCloudDataSource.fetchBooksFromByBookId(bookId = bookId)
        .map { savedBooks -> savedBooks.map { it.studentId } }
        .flatMapLatest { usersId ->
            cloudDataSource.fetchAllUsersFromUsersId(usersId = usersId, schoolId = schoolId)
        }.flowOn(dispatchersProvider.io())
        .map { users -> users.map(userCloudToDomainMapper::map) }
        .flowOn(dispatchersProvider.default())

    private fun handleCachedClassUsers(
        cachedUsers: List<UserCache>,
        classId: String,
        schoolId: String,
        currentUserId: String
    ) = if (cachedUsers.isEmpty()) cloudDataSource.fetchAllUsersFromId(
        schoolId = schoolId,
        responseType = StudentsResponseType.ClASS_STUDENTS
    ).onEach(cacheDataSource::saveStudents)
        .map { users -> users.filter { it.classId == classId } }
        .map { users -> users.filter { it.objectId != currentUserId } }
        .flowOn(dispatchersProvider.io())
    else cacheDataSource.fetchAllStudentsFromClassIdObservable(classId = classId)
        .flowOn(dispatchersProvider.io())
        .map(::mapUserCacheList)
        .map { users -> users.filter { it.objectId != currentUserId } }
        .flowOn(dispatchersProvider.default())

    private fun mapUserCacheList(users: List<UserCache>) = users.map(userCacheMapper::map)

    private fun mapUserDataList(users: List<StudentData>) = users.map(studentDomainMapper::map)

    override fun fetchSchoolStudents(schoolId: String): Flow<List<StudentDomain>> = flow {
        emit(cacheDataSource.fetchAllStudentsFromSchoolIdSingle(schoolId))
    }.flatMapLatest {
        handleCachedSchoolUsers(it, schoolId)
    }.map(::mapUserDataList)
        .flowOn(dispatchersProvider.default())

    override suspend fun fetchUserFromId(userId: String): StudentDomain =
        studentDomainMapper.map(userCacheMapper.map(cacheDataSource.fetchUserFromId(userId = userId)))

    private fun handleCachedSchoolUsers(cachedUsers: List<UserCache>, schoolId: String) =
        if (cachedUsers.isEmpty()) cloudDataSource.fetchAllUsersFromId(
            schoolId = schoolId,
            responseType = StudentsResponseType.SCHOOL_STUDENTS
        ).onEach(cacheDataSource::saveStudents)
            .flowOn(dispatchersProvider.io())
        else cacheDataSource.fetchAllStudentsFromSchoolIdObservable(schoolId = schoolId)
            .flowOn(dispatchersProvider.io())
            .map(::mapUserCacheList)
            .flowOn(dispatchersProvider.default())

    override fun onRefresh(classId: String): Flow<Resource<List<StudentDomain>>> = flow {
//        val result = cloudDataSource.fetchAllUsersFromClassId(
//            id = classId,
//            responseType = StudentsResponseType.ClASS_STUDENTS
//        )
//        if (result.status == Status.SUCCESS) {
//            val userDataList = result.data!!
//            if (userDataList.isEmpty()) emit(Resource.empty())
//            else {
//                cacheDataSource.saveStudents(userDataList)
//                val userDomainList =
//                    userDataList.map { userData -> studentDomainMapper.map(userData) }
//                emit(Resource.success(data = userDomainList))
//            }
//        } else emit(Resource.error(message = result.message!!))
    }


    override suspend fun updateStudentClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ) = renderResult(
        result = cloudDataSource.updateClass(
            id = id,
            sessionToken = sessionToken,
            classId = classId,
            classTitle = classTitle
        )
    )

    override suspend fun addSessionToken(id: String, sessionToken: String) = renderResultToUnit(
        result = cloudDataSource.addSessionToken(id = id, sessionToken = sessionToken)
    )

    override fun getCurrentUserWithCloud(sessionToken: String): Flow<UserDomain> =
        cloudDataSource.getCurrentUser(sessionToken = sessionToken)
            .map(userCloudToDomainMapper::map)
            .flowOn(dispatchersProvider.default())

    override suspend fun clearStudentsCache() {
        Log.i("Joseph", "clearStudentsCache")
        cacheDataSource.clearStudents()
    }

}

enum class StudentsResponseType {
    ClASS_USERS,
    ClASS_STUDENTS,
    SCHOOL_STUDENTS
}