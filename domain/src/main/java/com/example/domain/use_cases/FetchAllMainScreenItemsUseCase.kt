package com.example.domain.use_cases

import com.example.domain.DispatchersProvider
import com.example.domain.models.MainScreenItems
import com.example.domain.repository.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

interface FetchAllMainScreenItemsUseCase {

    operator fun invoke(): Flow<MainScreenItems>
}

@OptIn(ExperimentalCoroutinesApi::class)
class FetchAllMainScreenItemsUseCaseImpl(
    private val dispatchersProvider: DispatchersProvider,
    userCacheRepository: UserCacheRepository,
    userRepository: UserRepository,
    booksRepository: BooksRepository,
    savedBooksRepository: BookThatReadRepository,
    audioBooksRepository: AudioBooksRepository,
    tasksRepository: TasksRepository,
    genresRepository: GenresRepository,
    checkBookIsSavedAndSaveUseCase: CheckBookIsSavedAndSaveUseCase,
) : FetchAllMainScreenItemsUseCase {

    override fun invoke(): Flow<MainScreenItems> = combine(
        allBooksFlow,
        userSavedBooksFlow,
        allClassUsersAndIsTeacherFlow,
        audioBooksAndTasksFlow,
        allGenresFlow
    ) { allBooks, savedBooks, usersAndIsTeacher, audioBooksAndTasks, genres ->
        MainScreenItems(
            books = allBooks,
            savedBooks = savedBooks,
            isTeacher = usersAndIsTeacher.second,
            users = usersAndIsTeacher.first,
            audioBooks = audioBooksAndTasks.first,
            tasks = audioBooksAndTasks.second,
            genres = genres
        )
    }.flowOn(dispatchersProvider.default())

    private val currentUserFlow = userCacheRepository.fetchCurrentUserFromCache()
        .flowOn(dispatchersProvider.io())

    private val allAudioBooks = currentUserFlow.map { it.schoolId }
        .flatMapLatest(audioBooksRepository::fetchAllAudioBooks)

    private val allTasksFlow =
        currentUserFlow.map { it.classId }.flatMapLatest(tasksRepository::fetchAllClassTasks)

    private val allGenresFlow = genresRepository.fetchAllGenres()

    private val audioBooksAndTasksFlow = allAudioBooks.combine(allTasksFlow)
    { audioBooks, tasks -> Pair(audioBooks, tasks) }

    private
    val allClassUsers = currentUserFlow.flatMapLatest {
        userRepository.fetchAllStudentsFromClassId(
            classId = it.classId,
            schoolId = it.schoolId,
            currentUserId = it.id
        )
    }.flowOn(dispatchersProvider.io())

    private val allClassUsersAndIsTeacherFlow = currentUserFlow
        .map { user -> Pair(user.userType == TEACHER, user.classId) }
        .combine(allClassUsers) { isTeacher, users -> Pair(users, isTeacher.first) }
        .flowOn(dispatchersProvider.default())

    private val allBooksFlow = currentUserFlow.map { Pair(it.schoolId, it.id) }
        .flowOn(dispatchersProvider.default())
        .flatMapLatest { booksRepository.fetchAllBooks(schoolId = it.first, userId = it.second) }
        .flowOn(dispatchersProvider.io())

    private val userSavedBooksFlow = currentUserFlow.map { it.id }
        .flowOn(dispatchersProvider.default())
        .flatMapLatest(savedBooksRepository::fetchUserAllBooksThatReadByUserId)
        .onEach(checkBookIsSavedAndSaveUseCase::invoke)
        .flowOn(dispatchersProvider.io())

    private val allAndSavedBooksFlow = allBooksFlow
        .combine(userSavedBooksFlow) { allBooks, savedBooks -> Pair(allBooks, savedBooks) }
        .flowOn(dispatchersProvider.default())

    private companion object {
        const val TEACHER = "teacher"
    }
}

