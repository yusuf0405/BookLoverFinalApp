package com.example.domain.use_cases

import com.example.domain.sort_dialog.StudentSortOrder
import com.example.domain.models.StudentDomain
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface FetchAllSortedUsersUseCase {

    operator fun invoke(order: StudentSortOrder): Flow<List<StudentDomain>>
}

class FetchAllSortedUsersUseCaseImpl(
    userCacheRepository: UserCacheRepository,
    userRepository: UserRepository,
) : FetchAllSortedUsersUseCase {

    override fun invoke(order: StudentSortOrder) = allClassUsers.map { books ->
        fetchSortedBooksByOrder(order = order, users = books)
    }.flowOn(Dispatchers.Default)

    private val currentUserFlow = userCacheRepository
        .fetchCurrentUserFromCacheFlow()
        .flowOn(Dispatchers.IO)

    private val allClassUsers = currentUserFlow.flatMapLatest {
        userRepository.fetchAllStudentsFromClassId(
            classId = it.classId,
            schoolId = it.schoolId,
            currentUserId = it.id
        )
    }.flowOn(Dispatchers.IO)

    private fun fetchSortedBooksByOrder(
        order: StudentSortOrder,
        users: List<StudentDomain>
    ): List<StudentDomain> {
        return when (order) {
            StudentSortOrder.BY_LAST_NAME -> users.sortedBy { it.lastname }
            StudentSortOrder.BY_USER_NAME -> users.sortedBy { it.name }
            StudentSortOrder.BY_READ_MORE_BOOKS -> users.sortedByDescending { it.booksRead }
            StudentSortOrder.BY_READ_MORE_CHAPTERS -> users.sortedBy { it.chaptersRead }
            StudentSortOrder.BY_READ_MORE_PAGES -> users.sortedBy { it.progress }
        }
    }
}