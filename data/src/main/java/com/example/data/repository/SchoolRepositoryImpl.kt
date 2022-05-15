package com.example.data.repository

import com.example.data.ResourceProvider
import com.example.data.cloud.service.SchoolService
import com.example.data.toClass
import com.example.data.toSchool
import com.example.domain.Resource
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SchoolRepositoryImpl(
    private val service: SchoolService,
    private val resourceProvider: ResourceProvider,
) : SchoolRepository {

    override fun getAllSchools(): Flow<Resource<List<SchoolDomain>>> = flow {
        emit(service.getAllSchools())
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.school.map { it.toSchool() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            emit(Resource.error(message = resourceProvider.errorType(ex)))
        }
        .flowOn(Dispatchers.Default)

}