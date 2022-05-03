package com.example.bookloverfinalapp.app.models

import com.example.bookloverfinalapp.app.base.Abstract
import java.util.*

open class StudentAdapterModel : Abstract.Object<Unit, StudentAdapterModel.UserStringMapper>() {

    override fun map(mapper: UserStringMapper) {}

    object Progress : StudentAdapterModel()

    object Empty : StudentAdapterModel()

    class Fail(val message: String) : StudentAdapterModel() {
        override fun map(mapper: UserStringMapper) {
            mapper.map(message = message)
        }
    }

    class Base(
        var objectId: String,
        var classId: String,
        var createAt: Date,
        var schoolName: String,
        var className: String,
        var email: String,
        var gender: String,
        var lastname: String,
        var name: String,
        var number: String,
        var userType: String,
        var chaptersRead: Int,
        var booksRead: Int,
        var progress: Int,
        val booksId: List<String>,
        var image: StudentImage? = null,
    ) : StudentAdapterModel() {

        override fun map(mapper: UserStringMapper) {
            mapper.map(
                objectId = objectId,
                classId = classId,
                createAt = createAt,
                schoolName = schoolName,
                className = className,
                email = email,
                gender = gender,
                lastname = lastname,
                name = name,
                number = number,
                userType = userType,
                chaptersRead = chaptersRead,
                booksRead = booksRead,
                progress = progress,
                booksId = booksId,
                image = image,
            )
        }
    }

    interface UserStringMapper : Abstract.Mapper {
        fun map(message: String)
        fun map(
            objectId: String,
            classId: String,
            createAt: Date,
            schoolName: String,
            className: String,
            email: String,
            gender: String,
            lastname: String,
            name: String,
            number: String,
            userType: String,
            chaptersRead: Int,
            booksRead: Int,
            progress: Int,
            booksId: List<String>,
            image: StudentImage? = null,
        )
    }

}