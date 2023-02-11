package com.example.domain.use_cases

import com.example.domain.repository.*

interface ClearAllAppCacheUseCase {

    suspend operator fun invoke()

}

class ClearAllAppCacheUseCaseImpl(
    private val userRepository: UserRepository,
    private val booksRepository: BooksRepository,
    private val audioBooksRepository: AudioBooksRepository,
    private val bookThatReadRepository: BookThatReadRepository,
    private var classRepository: ClassRepository
) : ClearAllAppCacheUseCase {

    override suspend operator fun invoke() {
//        userRepository.clearStudentsCache()
//        booksRepository.clearBooksCache()
//        bookThatReadRepository.clearBooksCache()
//        audioBooksRepository.clearTable()
//        classRepository.clearTable()
    }
}
