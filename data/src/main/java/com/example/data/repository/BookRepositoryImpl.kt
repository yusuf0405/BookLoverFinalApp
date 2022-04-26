package com.example.data.repository

import com.example.data.api.KnigolyubApi
import com.example.data.base.BaseApiResponse
import com.example.data.toBookQuestion
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.book.BookQuestion
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookRepositoryImpl(private val api: KnigolyubApi) : BookRepository, BaseApiResponse() {


    override fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestion>>> = flow {
        emit(Resource.loading())
        val result =
            safeApiCall { api.getAllChapterQuestions(id = "{\"bookId\":\"$id\",\"chapter\":\"$chapter\"}") }
        when (result.status) {
            Status.SUCCESS -> {
                if (result.data!!.questions.isNotEmpty()) emit(Resource.success(data = result.data!!.questions.map { it.toBookQuestion() }))
                else emit(Resource.error(message = "No questions!!"))
            }
            Status.NETWORK_ERROR -> emit(Resource.networkError())
            else -> emit(Resource.error(message = result.message))
        }
    }
}
