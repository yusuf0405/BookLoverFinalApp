package com.example.data.data.cloud.source

import com.example.data.data.base.BaseApiResponse
import com.example.data.data.cloud.mappers.StudentBookMapper
import com.example.data.data.cloud.mappers.UserMapper
import com.example.data.data.cloud.service.UserService
import com.example.data.data.models.StudentData
import com.example.data.data.models.UserImageData
import com.example.domain.models.Resource
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface UsersCloudDataSource {

    suspend fun fetchMyStudents(className: String, schoolName: String): Resource<List<StudentData>>

    class Base(
        private val service: UserService,
    ) : UsersCloudDataSource, BaseApiResponse() {

        override suspend fun fetchMyStudents(
            className: String,
            schoolName: String,
        ): Resource<List<StudentData>> = try {
            val studentList = mutableListOf<StudentData>()
            val user =
                service.fetchMyBook(id = "{\"userType\":\"student\",\"classsName\":\"$className\",\"schoolName\":\"$schoolName\"}")

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

                val attributes =
                    StudentBookMapper.Base(chaptersRead = chapterRead,
                        booksRead = booksRead,
                        progress = progress,
                        booksId = booksId)

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
                            type = userCloud.image.type),
                        email = userCloud.email,
                        gender = userCloud.gender,
                        lastname = userCloud.lastname,
                        name = userCloud.name,
                        number = userCloud.number)

                studentList.add(student.map(StudentBookMapper.ComplexMapper(attributes)))

            }
            Resource.success(studentList)

        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> Resource.error(message = "Нету подключение к интернету")
                is SocketTimeoutException -> Resource.error(message = "Нету подключение к интернету")
                is HttpException -> Resource.error(message = "Ошибка Сервера")
                else -> Resource.error(message = "Что то пошло не так")
            }
        }
    }
}