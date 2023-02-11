package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.GenreBlockButtonItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.PopularGenreBlockItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.UserBlockAdapterItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.*
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.data.cache.models.IdResourceString
import com.example.domain.Mapper
import com.example.domain.models.*
import javax.inject.Inject

interface MainItemsToSearchFilteredModelMapper {

    fun map(
        items: MainScreenItems,
        bookItemOnClickListener: BookItemOnClickListener,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        savedBookItemOnClickListener: SavedBookItemOnClickListeners,
        userItemOnClickListener: UserItemOnClickListener,
        genreItemOnClickListeners: GenreItemOnClickListeners,
        openMoreClickListeners: MainScreenOpenMoreClickListeners,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ): Triple<List<Item>, List<Item>, List<Item>>

}

class MainItemsToSearchFilteredModelMapperImpl @Inject constructor(
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val userDomainToUserUiMapper: Mapper<StudentDomain, Student>,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>,
    private val userDomainToAdapterModelMapper: UserDomainToAdapterModelMapper,
    private val taskDomainToAdapterModelMapper: Mapper<TaskDomain, TaskAdapterModel>
) : MainItemsToSearchFilteredModelMapper {
    private companion object {
        const val MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN = 5
    }

    override fun map(
        items: MainScreenItems,
        bookItemOnClickListener: BookItemOnClickListener,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        savedBookItemOnClickListener: SavedBookItemOnClickListeners,
        userItemOnClickListener: UserItemOnClickListener,
        genreItemOnClickListeners: GenreItemOnClickListeners,
        openMoreClickListeners: MainScreenOpenMoreClickListeners,
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners
    ): Triple<List<Item>, List<Item>, List<Item>> {

        val filteredBooksList = items.books
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
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val filteredAudioBooksList = items.audioBooks
            .map(audioBookDomainToUiMapper::map)
            .map { AudioBookAdapterModel(it, audioBookItemOnClickListener) }
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val savedFilteredBooksList = items
            .savedBooks
            .map(savedBookDomainToUiMapper::map)
            .map { SavedBookAdapterModel(it, savedBookItemOnClickListener) }
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val userFilteredList = items.users
            .map(userDomainToUserUiMapper::map)
            .map { userDomainToAdapterModelMapper.map(it, userItemOnClickListener) }
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val tasks = items.tasks
            .map(taskDomainToAdapterModelMapper::map)
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

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

        val popularGenreItems = mutableListOf<Item>()
        listOf(filteredAudioBooksList, filteredBooksList)
        popularGenreItems.addAll(filteredAudioBooksList.shuffled())
        popularGenreItems.addAll(filteredBooksList.shuffled())
        val popularGenreBlockItem = PopularGenreBlockItem(items = popularGenreItems.shuffled())
        allAdapterItems.addAll(listOf(popularGenreBlockItem))

        val savedHorizontalItem = HorizontalItem(savedFilteredBooksList)
        if (savedHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForSavedBooks { openMoreClickListeners.navigateToAllSavedBooksFragment() })
        allAdapterItems.addAll(listOf(savedHorizontalItem))


        val audioBooksHorizontalItem = MainScreenAudioBookBlockItem(filteredAudioBooksList)
        if (audioBooksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllAudioBooks { openMoreClickListeners.navigateToAllAudioBooksFragment() })
        allAdapterItems.addAll(listOf(audioBooksHorizontalItem))

        val exclusive = ExclusiveBookHorizontalItem(filteredBooksList.shuffled())
        if (exclusive.items.isNotEmpty()) allAdapterItems.add(
            HeaderItem(
                titleId = IdResourceString(R.string.exclusive_books),
                onClickListener = {},
                showMoreIsVisible = false
            )
        )
        allAdapterItems.addAll(listOf(exclusive))
//        val tasksHorizontal = HorizontalItemSecond(tasks)
//        if (tasksHorizontal.items.isNotEmpty()) allAdapterItems.add(
//            createHeaderModelForAllTasks { openMoreClickListeners.navigateToAllTasksFragment() })
//        allAdapterItems.addAll(listOf(tasksHorizontal))

        val booksHorizontalItem = BookHorizontalItem(filteredBooksList)
        if (booksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllBooks { openMoreClickListeners.navigateToAllBooksFragment() })
        allAdapterItems.addAll(listOf(booksHorizontalItem))

        val allGenreBlockItems = mutableListOf<Item>()
        if (genres.isNotEmpty()) allGenreBlockItems.add(
            HeaderItem(
                titleId = IdResourceString(R.string.genres),
                onClickListener = {},
                showMoreIsVisible = false
            )
        )
        allGenreBlockItems.addAll(genres)

        if (genres.isNotEmpty()) allGenreBlockItems.add(
            GenreBlockButtonItem(onClickListener = { openMoreClickListeners.navigateToAllGenresFragment() })
        )
        val users = UserBlockAdapterItem(userFilteredList)
        val allUserBlockItems = mutableListOf<Item>()
        val headerTitle = if (items.isTeacher) (R.string.my_students) else (R.string.classmates)
        if (users.items.isNotEmpty()) allUserBlockItems.add(
            createHeaderModelForUsers(headerTitle) { openMoreClickListeners.navigateToAllStudentsFragment() })
        allUserBlockItems.add(users)
        return Triple(allAdapterItems, allGenreBlockItems, allUserBlockItems)
    }

    private fun createHeaderModelForSavedBooks(navigateToAllSavedBooksFragment: () -> Unit) =
        HeaderItem(
            titleId = IdResourceString(R.string.saved_books),
            onClickListener = { navigateToAllSavedBooksFragment() })

    private fun createHeaderModelForUsers(
        headerTitle: Int,
        navigateToAllStudentsFragment: () -> Unit
    ) = HeaderItem(
        titleId = IdResourceString(headerTitle),
        onClickListener = { navigateToAllStudentsFragment() })

    private fun createHeaderModelForAllBooks(navigateToAllBooksFragment: () -> Unit) =
        HeaderItem(
            titleId = IdResourceString(R.string.all_books),
            onClickListener = { navigateToAllBooksFragment() })

    private fun createHeaderModelForAllAudioBooks(navigateToAllAudioBooksFragment: () -> Unit) =
        HeaderItem(
            titleId = IdResourceString(R.string.all_audio_books),
            onClickListener = { navigateToAllAudioBooksFragment() })

    private fun createHeaderModelForAllTasks(navigateToAllAudioBooksFragment: () -> Unit) =
        HeaderItem(
            titleId = IdResourceString(R.string.all_tasks),
            onClickListener = { navigateToAllAudioBooksFragment() })
}