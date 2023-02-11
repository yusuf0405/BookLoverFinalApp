package com.example.domain.use_cases

import com.example.domain.models.BookDomain
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow

interface FetchBooksFromCloudUseCase {

    operator fun invoke(schoolId: String): Flow<List<BookDomain>>
}

class FetchBooksFromCloudUseCaseImpl(private var repository: BooksRepository) :
    FetchBooksFromCloudUseCase {

    override fun invoke(schoolId: String) = repository.fetchBooksFromCloud(schoolId = schoolId,"")


}