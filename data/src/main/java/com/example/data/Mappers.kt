package com.example.data

import com.example.data.cloud.models.UserSignUpCloud
import com.example.data.cloud.models.ClassCloud
import com.example.data.models.school.SchoolCloud
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain


internal fun UserSignUpDomain.toDtoSignUp(): UserSignUpCloud =
    UserSignUpCloud(
        name = name,
        lastname = lastname,
        email = email,
        password = password,
        number = number,
        className = className,
        schoolName = schoolName,
        gender = gender,
        classId = classId,
        userType = userType,
        username = email,
        schoolId = schoolId
    )


internal fun ClassCloud.toClass(): ClassDomain =
    ClassDomain(id = objectId, title = title, )


internal fun SchoolCloud.toSchool(): SchoolDomain =
    SchoolDomain(objectId = objectId, title = title, classesIds = classes, )
