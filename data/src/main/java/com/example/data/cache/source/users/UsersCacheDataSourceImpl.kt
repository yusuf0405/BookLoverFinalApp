package com.example.data.cache.source.users

import com.example.data.cache.db.UsersDao
import com.example.data.cache.models.UserCache
import com.example.data.models.StudentData
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersCacheDataSourceImpl @Inject constructor(
    private val dao: UsersDao,
    private val studentDataToCacheMapper: Mapper<StudentData, UserCache>,
) : UsersCacheDataSource {

    override fun fetchAllStudentsFromClassIdObservable(classId: String): Flow<List<UserCache>> =
        dao.fetchAllStudentsFromClassIdObservable(classId = classId)

    override suspend fun fetchAllStudentsFromClassIdSingle(classId: String): List<UserCache> =
        dao.fetchAllStudentsFromClassIdSingle(classId = classId)

    override suspend fun fetchUserFromId(userId: String): UserCache = dao.fetchUserFromId(userId)

    override fun fetchAllStudentsFromSchoolIdObservable(schoolId: String): Flow<List<UserCache>> =
        dao.fetchAllStudentsFromSchoolObservables(schoolId = schoolId)

    override suspend fun fetchAllStudentsFromSchoolIdSingle(schoolId: String): List<UserCache> =
        dao.fetchAllStudentsFromSchoolIdSingle(schoolId)

    override suspend fun saveStudents(users: List<StudentData>) {
        users.forEach { userData -> dao.addNewUser(user = studentDataToCacheMapper.map(userData)) }
    }

    override suspend fun deleteUser(id: String) = dao.deleteByUserId(id = id)

    override suspend fun clearStudents() = dao.clearTable()
}