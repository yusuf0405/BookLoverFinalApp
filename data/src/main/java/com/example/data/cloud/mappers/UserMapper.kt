package com.example.data.cloud.mappers

import com.example.data.models.UserImageData
import java.util.*

interface UserMapper {
    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(
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
        ): T
    }

    class Base(
        var objectId: String,
        var classId: String,
        var createAt: Date,
        var schoolName: String,
        var image: UserImageData,
        var className: String,
        var email: String,
        var gender: String,
        var lastname: String,
        var name: String,
        var number: String,
        var userType: String,
    ) : UserMapper {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
            objectId = objectId,
            classId = classId,
            createAt = createAt,
            schoolName = schoolName,
            image = image,
            className = className,
            email = email,
            gender = gender,
            lastname = lastname,
            name = name,
            number = number,
            userType = userType,)
    }
}