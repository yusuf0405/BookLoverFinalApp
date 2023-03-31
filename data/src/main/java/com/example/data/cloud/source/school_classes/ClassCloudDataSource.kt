package com.example.data.cloud.source.school_classes

import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.models.ClassData
import kotlinx.coroutines.flow.Flow

interface ClassCloudDataSource {

    fun getAllClass(schoolId: String): Flow<List<ClassData>>

    suspend fun deleteClass(id: String): CloudDataRequestState<Unit>

    suspend fun addClass(title: String, schoolId: String): CloudDataRequestState<PostRequestAnswerCloud>

    fun fetchUserClassesFromId(id: String): Flow<List<ClassData>>
}