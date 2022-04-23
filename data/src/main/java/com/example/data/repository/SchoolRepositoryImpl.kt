package com.example.data.repository

import com.example.data.api.KnigolyubApi
import com.example.data.toClass
import com.example.data.toSchool
import com.example.domain.models.Resource
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException

class SchoolRepositoryImpl(private val api: KnigolyubApi) : SchoolRepository {

    override fun getAllClasses(): Flow<Resource<List<Class>>> = flow {
        emit(api.getAllClasses())
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.classes.map { it.toClass() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message))
            }
        }.flowOn(Dispatchers.Default)

    override fun getAllSchools(): Flow<Resource<List<School>>> = flow {
        emit(api.getAllSchools())
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.school.map { it.toSchool() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message))
            }
        }.flowOn(Dispatchers.Default)

    override fun getClass(id: String): Flow<Resource<List<Class>>> = flow {
        emit(api.getClass(id = "{\"objectId\":\"$id\"}"))
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.classes.map { it.toClass() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message))
            }
        }.flowOn(Dispatchers.Default)
}