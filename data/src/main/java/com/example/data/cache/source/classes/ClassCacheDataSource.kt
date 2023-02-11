package com.example.data.cache.source.classes

import com.example.data.cache.models.ClassCache
import com.example.data.models.ClassData
import kotlinx.coroutines.flow.Flow

interface ClassCacheDataSource {

    fun fetchAllClass(): Flow<List<ClassCache>>

    suspend fun saveClasses(classes: List<ClassData>)

    suspend fun addClass(schoolClass: ClassData)

    suspend fun deleteClass(id: String)

    suspend fun clearTable()
}