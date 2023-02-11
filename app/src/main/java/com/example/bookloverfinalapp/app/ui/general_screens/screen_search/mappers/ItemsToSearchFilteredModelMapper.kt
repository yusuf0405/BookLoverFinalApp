package com.example.bookloverfinalapp.app.ui.general_screens.screen_search.mappers

import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.UserBlockAdapterItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.*
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.data.cache.models.IdResourceString
import com.example.domain.Mapper
import com.example.domain.models.*
import javax.inject.Inject

interface ItemsToSearchFilteredModelMapper {

    fun map(
        items: MainScreenItems,
        searchQuery: String,
        bookItemOnClickListener: BookItemOnClickListener,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        savedBookItemOnClickListener: SavedBookItemOnClickListeners,
        userItemOnClickListener: UserItemOnClickListener,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ): List<Item>
}

class ItemsToSearchFilteredModelMapperImpl @Inject constructor(
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val userDomainToUserUiMapper: Mapper<StudentDomain, Student>,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>,
    private val userDomainToAdapterModelMapper: UserDomainToAdapterModelMapper,
    private val taskDomainToAdapterModelMapper: Mapper<TaskDomain, TaskAdapterModel>
) :
    ItemsToSearchFilteredModelMapper {

    override fun map(
        items: MainScreenItems,
        searchQuery: String,
        bookItemOnClickListener: BookItemOnClickListener,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        savedBookItemOnClickListener: SavedBookItemOnClickListeners,
        userItemOnClickListener: UserItemOnClickListener,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ): List<Item> {

        val filteredBooksList = items.books
            .filter { applyFilterForAllBooks(it, searchQuery) }
            .map(bookDomainToBookMapper::map)
            .map {
                HorizontalBookAdapterModel(
                    id = it.id,
                    posterUrl = it.poster.url,
                    author = it.author,
                    description = it.description,
                    savedStatus = it.savedStatus,
                    title = it.title,
                    listener = bookItemOnClickListener
                )
            }

        val filteredAudioBooksList = items.audioBooks
            .filter { applyFilterForAllAudioBooks(it, searchQuery) }
            .map(audioBookDomainToUiMapper::map)
            .map { AudioBookAdapterModel(it, audioBookItemOnClickListener) }

        val savedFilteredBooksList = items
            .savedBooks
            .map(savedBookDomainToUiMapper::map)
            .filter { applyFilterForSavedBooks(it, searchQuery) }
            .map { SavedBookAdapterModel(it, savedBookItemOnClickListener) }

        val userFilteredList = items.users
            .map(userDomainToUserUiMapper::map)
            .filter { applyFilterForUsers(it, searchQuery) }
            .map { userDomainToAdapterModelMapper.map(it, userItemOnClickListener) }

        val tasks = items.tasks
            .map(taskDomainToAdapterModelMapper::map)

        val genres = items.genres.swapElements()
            .take(4).map { genre ->
                BookGenreAdapterModel(
                    id = genre.id,
                    titles = genre.titles,
                    posterUrl = genre.poster.url,
                    listener = bookGenreItemOnClickListeners
                )
            }
        val allAdapterItems = mutableListOf<Item>()

        val savedHorizontalItem = HorizontalItem(savedFilteredBooksList)
        if (savedHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForSavedBooks()
        )
        allAdapterItems.addAll(listOf(savedHorizontalItem))


        val audioBooksHorizontalItem = MainScreenAudioBookBlockItem(filteredAudioBooksList)
        if (audioBooksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllAudioBooks()
        )
        allAdapterItems.addAll(listOf(audioBooksHorizontalItem))

//        val tasksHorizontal = HorizontalItemSecond(tasks)
//        if (tasksHorizontal.items.isNotEmpty()) allAdapterItems.add(
//            createHeaderModelForAllTasks { openMoreClickListeners.navigateToAllTasksFragment() })
//        allAdapterItems.addAll(listOf(tasksHorizontal))

        val booksHorizontalItem = BookHorizontalItem(filteredBooksList)
        if (booksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllBooks()
        )
        allAdapterItems.addAll(listOf(booksHorizontalItem))

//        if (genres.isNotEmpty()) al.add(
//            HeaderItem(
//                titleId = IdResourceString(R.string.genres),
//                onClickListener = {},
//                showMoreIsVisible = false
//            )
//        )
//        allGenreBlockItems.addAll(genres)
//
//        if (genres.isNotEmpty()) allGenreBlockItems.add(
//            GenreBlockButtonItem(onClickListener = { openMoreClickListeners.navigateToAllGenresFragment() })
//        )
        val users = UserBlockAdapterItem(userFilteredList)
        val headerTitle = if (items.isTeacher) (R.string.my_students) else (R.string.classmates)
        if (users.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForUsers(headerTitle)
        )
        allAdapterItems.add(users)
        return allAdapterItems
    }

    private fun applyFilterForAllAudioBooks(book: AudioBookDomain, searchQuery: String) =
        if (searchQuery.isEmpty()) book.title != String()
        else book.title.contains(searchQuery, ignoreCase = true)

    private fun applyFilterForAllBooks(book: BookDomain, searchQuery: String) =
        if (searchQuery.isEmpty()) book.title != String()
        else book.title.contains(searchQuery, ignoreCase = true)

    private fun applyFilterForAllGenres(genre: GenreAdapterModel, searchQuery: String) =
        if (searchQuery.isEmpty()) genre.genreCode != String()
        else genre.genreTitle.contains(searchQuery, ignoreCase = true)

    private fun applyFilterForSavedBooks(book: BookThatRead, searchQuery: String) =
        if (searchQuery.isEmpty()) book != BookThatRead.unknown()
        else book.title.contains(searchQuery, ignoreCase = true)

    private fun applyFilterForUsers(user: Student, searchQuery: String) =
        if (searchQuery.isEmpty()) user.userType != UserType.unknown.name
        else user.fullName().contains(searchQuery, ignoreCase = true)

    private fun createHeaderModelForSavedBooks() =
        HeaderItem(
            titleId = IdResourceString(R.string.saved_books),
            showMoreIsVisible = false,
            onClickListener = { })

    private fun createHeaderModelForUsers(
        headerTitle: Int,
    ) = HeaderItem(
        titleId = IdResourceString(headerTitle),
        onClickListener = {},
        showMoreIsVisible = false
    )

    private fun createHeaderModelForAllBooks() =
        HeaderItem(
            titleId = IdResourceString(R.string.all_books),
            onClickListener = { },
            showMoreIsVisible = false
        )

    private fun createHeaderModelForAllAudioBooks() =
        HeaderItem(
            titleId = IdResourceString(R.string.all_audio_books),
            onClickListener = { },
            showMoreIsVisible = false
        )

    private fun createHeaderModelForAllTasks() =
        HeaderItem(
            titleId = IdResourceString(R.string.all_tasks),
            onClickListener = { },
            showMoreIsVisible = false
        )
}