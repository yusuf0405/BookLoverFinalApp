package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.base.ResponseHandler
import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.AddClassCloud
import com.example.data.cloud.models.ClassCloud
import com.example.data.cloud.models.ClassResponse
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.cloud.service.ClassService
import com.example.data.models.ClassData
import com.example.domain.Mapper
import com.example.domain.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClassCloudDataSourceImpl @Inject constructor(
    private val service: ClassService,
    private val resourceProvider: ResourceProvider,
    private val mapper: Mapper<ClassCloud, ClassData>,
) : ClassCloudDataSource, BaseApiResponse(resourceProvider) {

    override fun getAllClass(schoolId: String) = flow {
        val response = safeApiCall { service.getClass(id = "{\"schoolId\":\"$schoolId\"}") }
        if (response.status == Status.SUCCESS) {
            emit(response.data!!.classes.map(mapper::map))
        }
    }

    override suspend fun deleteClass(id: String): CloudDataRequestState<Unit> =
        safeApiCalll { service.deleteClass(id = id) }

    override fun fetchUserClassesFromId(id: String): Flow<List<ClassData>> = flow {
        emit(service.getClass(id = "{\"objectId\":\"$id\"}"))
    }.flowOn(Dispatchers.IO)
        .map { it.body() ?: ClassResponse.unknown() }
        .map { it.classes }
        .map { classes -> classes.map(mapper::map) }
        .flowOn(Dispatchers.Default)

    override suspend fun addClass(
        title: String,
        schoolId: String,
    ): CloudDataRequestState<PostRequestAnswerCloud> = safeApiCalll {
        service.addBookQuestion(
            AddClassCloud(
                title = title,
                schoolId = schoolId
            )
        )
    }
}