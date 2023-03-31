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
    storiesRepository: StoriesRepository,
) : FetchAllMainScreenItemsUseCase {

    override fun invoke(): Flow<MainScreenItems> = combine(
        allBooksAndCurrentUserFlow,
        userSavedBooksFlow,
        allClassUsersAndIsTeacherFlow,
        allAudioBooks,
        allStoriesAndGenresFlow
    ) { allBooksAndCurrentUser, savedBooks, usersAndIsTeacher, audioBooks, genresAndStories ->
        MainScreenItems(
            currentUser = allBooksAndCurrentUser.second,
            books = allBooksAndCurrentUser.first,
            savedBooks = savedBooks,
            isTeacher = usersAndIsTeacher.second,
            users = usersAndIsTeacher.first,
            audioBooks = audioBooks,
            tasks = emptyList(),
            genres = genresAndStories.first,
            stories = genresAndStories.second,
        )
    }.flowOn(dispatchersProvider.default())

    private val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .flowOn(dispatchersProvider.io())

    private val allAudioBooks = currentUserFlow.map { it.schoolId }
        .flatMapLatest(audioBooksRepository::fetchAllAudioBooks)

    private val allTasksFlow =
        currentUserFlow.map { it.classId }.flatMapLatest(tasksRepository::fetchAllClassTasks)

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

    private val allBooksAndCurrentUserFlow = allBooksFlow
        .combine(currentUserFlow) { allBooks, user -> Pair(allBooks, user) }
        .flowOn(dispatchersProvider.default())


    private val userSavedBooksFlow = currentUserFlow.map { it.id }
        .flowOn(dispatchersProvider.default())
        .flatMapLatest(savedBooksRepository::fetchUserAllBooksThatReadByUserId)
        .flowOn(dispatchersProvider.io())

    private val allGenresFlow = genresRepository.fetchAllGenres()
        .flowOn(dispatchersProvider.io())

    private val storiesFlow = currentUserFlow.map { it.schoolId }
        .flatMapLatest(storiesRepository::fetchAllStories)
        .flowOn(dispatchersProvider.io())


    private val allStoriesAndGenresFlow = allGenresFlow.combine(storiesFlow) { genres, stories ->
        Pair(genres, stories)
    }.flowOn(dispatchersProvider.default())

    private val allAndSavedBooksFlow = allBooksFlow
        .combine(userSavedBooksFlow) { allBooks, savedBooks -> Pair(allBooks, savedBooks) }
        .flowOn(dispatchersProvider.default())


    private companion object {
        const val TEACHER = "teacher"
    }
}

