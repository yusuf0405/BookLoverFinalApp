package com.example.data.cloud.mappers

import com.example.data.models.StudentData
import com.example.data.models.StudentImageData
import com.example.data.models.UserImageData
import java.util.*

interface StudentBookMapper {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
            chaptersRead: Int,
            booksRead: Int,
            progress: Int,
            schoolId: String,
            booksId: List<String>,
        ): T
    }

    class Base(
        val chaptersRead: Int,
        val booksRead: Int,
        val progress: Int,
        val booksId: List<String>,
        val schoolId: String
    ) : StudentBookMapper {
        override fun <T> map(mapper: Mapper<T>): T =
            mapper.map(
                chaptersRead = chaptersRead,
                booksRead = booksRead,
                progress = progress,
                booksId = booksId,
                schoolId = schoolId
            )
    }

    class ComplexMapper(private val mapper: StudentBookMapper) : UserMapper.Mapper<StudentData> {
        override fun map(
            objectId: String,
            classId: String,
            createAt: Date,
            schoolName: String,
            image: UserImageData,
            className: String,
            email: String,
            gender: String,
            lastname: String,
            name: String,
            number: String,
            userType: String,
            sessionToken: String,
        ): StudentData = mapper.map(object : Mapper<StudentData> {
            override fun map(
                chaptersRead: Int,
                booksRead: Int,
                progress: Int,
                schoolId: String,
                booksId: List<String>,
            ): StudentData = StudentData(
                createAt = createAt,
                classId = classId,
                email = email,
                gender = gender,
                lastname = lastname,
                name = name,
                number = number,
                schoolName = schoolName,
                className = className,
                objectId = objectId,
                userType = userType,
                image = StudentImageData(name = image.name, type = image.type, url = image.url),
                booksId = booksId,
                progress = progress,
                booksRead = booksRead,
                chaptersRead = chaptersRead,
                sessionToken = sessionToken,
                schoolId = schoolId
            )

        })

    }
}