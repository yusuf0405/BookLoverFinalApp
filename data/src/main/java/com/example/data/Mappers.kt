package com.example.data

import com.example.data.models.classes.ClassDto
import com.example.data.models.school.SchoolDto
import com.example.data.models.student.*
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.*
import com.parse.ParseFile

internal fun UserDto.toUser(): UserDomain =
    UserDomain(
        createAt = createAt,
        classId = classId,
        email = email,
        gender = gender,
        lastname = lastname,
        name = name,
        number = number,
        schoolName = schoolName,
        className = className,
        id = objectId,
        image = image.toImage(),
        userType = userType,
        sessionToken = sessionToken
    )

internal fun UserDto.toStudentNoImage(): UserDomain =
    UserDomain(
        createAt = createAt,
        classId = classId,
        email = email,
        gender = gender,
        lastname = lastname,
        name = name,
        number = number,
        schoolName = schoolName,
        className = className,
        id = objectId,
        userType = userType, sessionToken = sessionToken
    )

internal fun StudentAddBook.toRequestBook(): StudentAddBookRequest =
    StudentAddBookRequest(books = books)

internal fun StudentAddBookRequest.toBook(): StudentAddBook =
    StudentAddBook(books = books)


internal fun UserSignUpRes.toDtoSignUp(): UserSignUpRequest =
    UserSignUpRequest(
        name = name,
        lastname = lastname,
        email = email,
        password = password,
        number = number,
        className = className,
        schoolName = schoolName,
        gender = gender,
        classId = classId,
        userType = userType
    )


internal fun StudentUpdateRes.toDtoStudent(): UserUpdateRequest =
    UserUpdateRequest(
        image = image.toDtoImage(),
        lastname = lastname,
        name = name,
        gender = gender,
        email = email,
        number = number
    )


internal fun UpdateAnswer.toDtoUpdate(): UpdateDto =
    UpdateDto(
        updatedAt = updatedAt
    )

internal fun UpdateDto.toDtoUpdate(): UpdateAnswer =
    UpdateAnswer(
        updatedAt = updatedAt
    )

internal fun ParseFile.toImage(): UserDomainImage =
    UserDomainImage(
        name = name,
        type = "File",
        url = url,
    )


private fun UserImageDto.toImage(): UserDomainImage =
    UserDomainImage(
        name = name,
        url = url,
        type = type
    )

private fun UserDomainImage.toDtoImage(): UserImageDto =
    UserImageDto(
        name = name,
        url = url,
        type = type
    )

internal fun ClassDto.toClass(): Class =
    Class(
        objectId = objectId,
        teachers = teachers,
        title = title,
        students = students
    )


internal fun SchoolDto.toSchool(): School =
    School(
        objectId = objectId,
        title = title,
        classesIds = classes,
    )

internal fun PostRequestAnswerDto.toRequestAnswer(): PostRequestAnswer =
    PostRequestAnswer(
        id = objectId,
        createdAt = createdAt,
        sessionToken = sessionToken,
        image = image.toImage()
    )

internal fun School.toSchoolDto(): SchoolDto =
    SchoolDto(
        objectId = objectId,
        title = title,
        classes = classesIds,
    )

