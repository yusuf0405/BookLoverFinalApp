package com.example.data.cache.source

import com.example.data.cache.db.ClassDao
import com.example.data.cache.models.ClassCache
import com.example.data.models.ClassData
import com.example.domain.Mapper

interface ClassCacheDataSource {

    suspend fun fetchAllClass(): List<ClassCache>

    suspend fun saveClasses(classes: List<ClassData>)

    suspend fun deleteClass(id: String)

    class Base(
        private val dao: ClassDao,
        private val mapper: Mapper<ClassData, ClassCache>,
    ) : ClassCacheDataSource {

        override suspend fun fetchAllClass(): List<ClassCache> = dao.getAllClass()

        override suspend fun saveClasses(classes: List<ClassData>) {
            classes.forEach { classData -> dao.addNewClass(schoolClass = mapper.map(classData)) }
        }

        override suspend fun deleteClass(id: String) = dao.deleteByClassId(id = id)
    }
}