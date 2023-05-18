package com.example.data.cloud.users

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.mappers.StudentBookMapper
import com.example.data.cloud.mappers.UserMapper
import com.example.data.cloud.models.*
import com.example.data.cloud.service.UserService
import com.example.data.models.StudentData
import com.example.data.models.UserImageData
import com.example.data.repository.users.StudentsResponseType
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.UpdateAnswerDomain
import com.example.domain.models.UserUpdateDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UsersCloudDataSourceImpl @Inject constructor(
    private val service: UserService,
    private val dispatchersProvider: DispatchersProvider,
    private val userToDataMapper: Mapper<UserUpdateDomain, UserUpdateCloud>,
    private val updateMapper: Mapper<UpdateCloud, UpdateAnswerDomain>,
    private val resourceProvider: ResourceProvider,
) : UsersCloudDataSource, BaseApiResponse(resourceProvider = resourceProvider) {


    override fun fetchAllUsersFromId(
        responseType: StudentsResponseType,
        schoolId: String,
    ): Flow<List<StudentData>> = flow {
        val studentList = mutableListOf<StudentData>()
        val user = when (responseType) {
            StudentsResponseType.ClASS_USERS -> service.fetchUsersFrom(id = "{\"schoolId\":\"$schoolId\"}")
            StudentsResponseType.SCHOOL_STUDENTS -> service.fetchUsersFrom(id = "{\"userType\":\"student\",\"schoolId\":\"$schoolId\"}")
            StudentsResponseType.ClASS_STUDENTS -> service.fetchUsersFrom(id = "{\"userType\":\"student\",\"schoolId\":\"$schoolId\"}")
        }
        user.body()!!.users.forEach { userCloud ->
            val response =
                service.fetchStudentAttributes("{\"userId\":\"${userCloud.objectId}\"}")

            var booksRead = 0
            var chapterRead = 0
            var progress = 0
            val booksId = mutableListOf<String>()
            response.body()!!.books.forEach { bookThatReadCloud ->
                if (bookThatReadCloud.isReadingPages[bookThatReadCloud.isReadingPages.lastIndex]) booksRead += 1
                chapterRead += bookThatReadCloud.chaptersRead
                progress += bookThatReadCloud.progress
                booksId.add(bookThatReadCloud.bookId)
            }

            val attributes = StudentBookMapper.Base(
                chaptersRead = chapterRead,
                booksRead = booksRead,
                progress = progress,
                booksId = booksId,
                schoolId = userCloud.schoolId
            )

            val student =
                UserMapper.Base(
                    objectId = userCloud.objectId,
                    className = userCloud.className,
                    createAt = userCloud.createAt,
                    classId = userCloud.classId,
                    userType = userCloud.userType,
                    schoolName = userCloud.schoolName,
                    image = UserImageData(
                        name = userCloud.image.name,
                        url = userCloud.image.url,
                        type = userCloud.image.type
                    ),
                    email = userCloud.email,
                    gender = userCloud.gender,
                    lastname = userCloud.lastname,
                    name = userCloud.name,
                    number = userCloud.number,
                    sessionToken = userCloud.userSessionToken
                )
            studentList.add(student.map(StudentBookMapper.ComplexMapper(attributes)))
        }
        emit(studentList)
    }

    override fun fetchAllUsersFromUsersId(
        usersId: List<String>,
        schoolId: String
    ): Flow<List<UserCloud>> = flow {
        val users = mutableListOf<UserCloud>()
        usersId.map { userId ->
            service.fetchUsersFrom(id = "{\"userType\":\"student\",\"schoolId\":\"$schoolId\",\"objectId\":\"$userId\"}")
        }.onEach {
            val user = it.body() ?: UserResponse(emptyList())
            users.add(user.users.first())
        }
        emit(users)
    }.flowOn(dispatchersProvider.io())

    override suspend fun updateUser(
        id: String,
        user: UserUpdateDomain,
        sessionToken: String,
    ) = safeApiCalll {
        service.updateUser(sessionToken = sessionToken, id = id, userToDataMapper.map(user))
    }.map(updateMapper)


    override suspend fun updateClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ): CloudDataRequestState<Unit> = safeApiCalll {
        service.updateStudentClass(
            sessionToken = sessionToken,
            id = id,
            updateClass = UpdateStudentClassCloud(classId = classId, classTitle = classTitle)
        )
    }

    override suspend fun deleteUser(id: String, sessionToken: String) = safeApiCalll {
        service.deleteUser(sessionToken = sessionToken, id = id)
    }

    override suspend fun addSessionToken(id: String, sessionToken: String) = safeApiCalll {
        service.addSessionToken(
            id = id,
            userSessionToken = SessionTokenCloud(sessionToken = sessionToken),
            sessionToken = sessionToken
        )
    }


    override fun getCurrentUser(sessionToken: String): Flow<UserCloud> = flow {
        emit(service.getCurrentUserInfo(sessionToken = sessionToken))
    }.flowOn(Dispatchers.IO)
        .map { it.body() ?: UserCloud.unknown() }
        .flowOn(Dispatchers.Default)
}