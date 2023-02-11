package com.example.domain.repository

import java.io.InputStream

interface BooksSaveToFileRepository {

    suspend fun saveBookToFile(
        inputStream: InputStream,
        key: String,
        fileName: String,
        fileSuffix: String
    )

    fun fetchSavedBookFilePath(key: String): String?
}