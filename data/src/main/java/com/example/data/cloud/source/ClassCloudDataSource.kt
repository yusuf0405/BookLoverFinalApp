package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.models.AddClassCloud
import com.example.data.cloud.models.ClassCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.cloud.service.ClassService
import com.example.data.models.ClassData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ClassCloudDataSource {

    suspend fun getAllClass(schoolId: String): Resource<List<ClassData>>

    suspend fun deleteClass(id: String): Resource<Unit>

    suspend fun addClass(title: String, schoolId: String): Resource<PostRequestAnswerCloud>

    fun getClass(id: String): Flow<Resource<Unit>>

    class Base(
        private val service: ClassService,
        resourceProvider: ResourceProvider,
        private val mapper: Mapper<ClassCloud, ClassData>,
    ) : ClassCloudDataSource, BaseApiResponse(resourceProvider) {

        override suspend fun getAllClass(schoolId: String): Resource<List<ClassData>> {
            val response = safeApiCall { service.getClass(id = "{\"schoolId\":\"$schoolId\"}") }
            return if (response.status == Status.SUCCESS)
                Resource.success(data = response.data!!.classes.map { classCloud ->
                    mapper.map(classCloud)
                })
            else Resource.error(message = response.message!!)
        }

        override suspend fun deleteClass(id: String): Resource<Unit> =
            safeApiCall { service.deleteClass(id = id) }

        override fun getClass(id: String): Flow<Resource<Unit>> = flow {
            val response = safeApiCall { service.getClass(id = "{\"objectId\":\"$id\"}") }
            if (response.status == Status.SUCCESS) {
                val classList = response.data!!.classes
                if (classList.isEmpty()) emit(Resource.empty())
                else emit(Resource.success(data = Unit))
            } else emit(Resource.error(message = response.message))
        }

        override suspend fun addClass(
            title: String,
            schoolId: String,
        ): Resource<PostRequestAnswerCloud> = safeApiCall {
            service.addBookQuestion(AddClassCloud(title = title,
                schoolId = schoolId))
        }
    }
}