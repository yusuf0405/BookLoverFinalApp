package com.example.data.repository

import com.example.data.*
import com.example.data.api.KnigolyubApi
import com.example.data.base.BaseApiResponse
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.book.*
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.models.student.UpdateAnswer
import com.example.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class BookRepositoryImpl(private val api: KnigolyubApi) : BookRepository, BaseApiResponse() {

    override fun getMyProgress(id: String): Flow<Resource<List<BooksThatRead>>> = flow {
        emit(api.studentMyProgress(id = "{\"userId\":\"${id}\"}"))
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.books.map { it.toBook() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message))
            }
        }.flowOn(Dispatchers.Default)


    override fun addNewStudentBook(book: AddNewBookRequest): Flow<Resource<PostRequestAnswer>> =
        flow {
            emit(api.addNewBookStudent(book = book))
        }.flowOn(Dispatchers.IO)
            .map { dto -> dto.body()!!.toRequestAnswer() }
            .map { list -> Resource.success(data = list) }
            .onStart { emit(Resource.loading()) }
            .catch { ex ->
                when (ex) {
                    is UnknownHostException -> emit(Resource.networkError())
                    else -> emit(Resource.error(ex.message))
                }
            }.flowOn(Dispatchers.Default)

    override fun getMyBook(
        userId: String,
        bookId: String,
    ): Flow<Resource<List<AddNewBookRequest>>> = flow {
        emit(api.getAllStudentBooks(id = "{\"userId\":\"$userId\",\"bookId\":\"$bookId\"}"))
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.results.map { it.toBook() } }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message ?: ex.localizedMessage))
            }
        }.flowOn(Dispatchers.Default)

    override fun updateProgressStudentBook(
        id: String,
        progress: BookUpdateProgress,
    ): Flow<Resource<UpdateAnswer>> = flow {
        emit(api.updateProgressStudentBook(id = id, progress = progress.toRequest()))
    }.flowOn(Dispatchers.IO)
        .map { dto -> dto.body()!!.toDtoUpdate() }
        .map { list -> Resource.success(data = list) }
        .onStart { emit(Resource.loading()) }
        .catch { ex ->
            when (ex) {
                is UnknownHostException -> emit(Resource.networkError())
                else -> emit(Resource.error(ex.message))
            }
        }.flowOn(Dispatchers.Default)

    override fun getBookForReading(url: String): Flow<Resource<InputStream>> = flow {
        emit(Resource.loading())
        val urlConnection = URL(url).openConnection() as HttpsURLConnection
        if (urlConnection.responseCode == 200) emit(Resource.success(data = BufferedInputStream(
            urlConnection.inputStream)))
        else emit(Resource.error(message = urlConnection.responseMessage))

    }


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
