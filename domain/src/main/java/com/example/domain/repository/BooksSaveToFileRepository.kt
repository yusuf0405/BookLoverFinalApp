package com.example.domain.repository

import kotlinx.coroutines.flow.Flow


interface BooksSaveToFileRepository {

    suspend fun saveBookToFile(
        fileUrl: String,
        key: String,
        fileName: String,
    ): Flow<String>

    fun fetchSavedBookFilePath(key: String): String?
}