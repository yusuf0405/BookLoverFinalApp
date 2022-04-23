package com.example.data.data.cloud.source

import com.example.data.base.BaseApiResponse
import com.example.data.data.cloud.models.BookResponse
import com.example.data.data.cloud.service.BookService
import com.example.domain.models.Resource

interface BooksCloudDataSource {

    suspend fun fetchBooks(): Resource<BookResponse>

    class Base(private val service: BookService) : BooksCloudDataSource, BaseApiResponse() {

        override suspend fun fetchBooks(): Resource<BookResponse> = safeApiCall { service.fetchAllBooks() }
    }
}
