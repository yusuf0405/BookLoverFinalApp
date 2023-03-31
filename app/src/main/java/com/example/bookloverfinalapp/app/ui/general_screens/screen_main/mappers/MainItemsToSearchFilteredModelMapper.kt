package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapper
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
import com.joseph.stories.presentation.mappers.StoriesToUserStoriesMapper
import com.joseph.stories.presentation.models.StoriesAddAdapterItem
import com.joseph.stories.presentation.models.StoriesItemOnClickListener
import com.joseph.stories.presentation.models.StoriesModel
import com.joseph.ui_core.adapter.Item
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
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners,
        storiesOnClickListener: StoriesItemOnClickListener,
        collectionItemOnClickListener: CollectionItemOnClickListener,
        selectFavoriteBooksItemOnClickListener: SelectFavoriteBooksItemOnClickListeners,
        addNewBooksItemOnClickListener: AddNewBooksItemOnClickListeners
    ): Triple<List<Item>, List<Item>, List<Item>>

}

class MainItemsToSearchFilteredModelMapperImpl @Inject constructor(
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val userDomainToUserUiMapper: Mapper<StudentDomain, Student>,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>,
    private val userDomainToAdapterModelMapper: UserDomainToAdapterModelMapper,
    private val taskDomainToAdapterModelMapper: Mapper<TaskDomain, TaskAdapterModel>,
    private val storiesToUserStoriesMapper: StoriesToUserStoriesMapper,
    private val storiesDomainToUiMapper: Mapper<StoriesDomain, StoriesModel>,
    private val bookToExclusiveBookMapper: BookToExclusiveBookMapper,
    private val bookToHorizontalBookMapper: BookToHorizontalBookMapper,
    private val genreMapBookGenreItemMapper: GenreMapBookGenreItemMapper,
    private val userStoriesToStoriesItemMapper: UserStoriesToStoriesItemMapper,
    private val audioBookToExclusiveAudioBookMapper: AudioBookToExclusiveAudioBookMapper,
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
        bookGenreItemOnClickListeners: BookGenreItemOnClickListeners,
        storiesOnClickListener: StoriesItemOnClickListener,
        collectionItemOnClickListener: CollectionItemOnClickListener,
        selectFavoriteBooksItemOnClickListener: SelectFavoriteBooksItemOnClickListeners,
        addNewBooksItemOnClickListener: AddNewBooksItemOnClickListeners
    ): Triple<List<Item>, List<Item>, List<Item>> {
        val allFilteredBooksList = items.books.map(bookDomainToBookMapper::map)

        val limitedBooks = allFilteredBooksList.map {
            bookToHorizontalBookMapper(it, bookItemOnClickListener)
        }.take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val filteredBooksList = allFilteredBooksList.take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val exclusiveBooks = allFilteredBooksList.filter { it.isExclusive }.map {
            bookToExclusiveBookMapper(it, bookItemOnClickListener)
        }

        val allAudioBooksList = items.audioBooks.map(audioBookDomainToUiMapper::map)

        val limitedAudioBooks = allAudioBooksList
            .map { AudioBookAdapterModel(it, audioBookItemOnClickListener) }
            .take(MAX_ITEMS_SHOW_COUNT_IN_MAIN_SCREEN)

        val exclusiveAudioBooks = allAudioBooksList.filter { it.isExclusive }.map {
            audioBookToExclusiveAudioBookMapper(it, audioBookItemOnClickListener)
        }

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
            .take(4)
            .map { genre -> genreMapBookGenreItemMapper(genre, bookGenreItemOnClickListeners) }

        val allAdapterItems = mutableListOf<Item>()

        val allCollections = Collections.fetchAllCollections().map {
            CollectionItem(collections = it, listener = collectionItemOnClickListener)
        }
        val addStories =
            createAddStoriesAdapterItem(storiesOnClickListener)

        val stories = items.stories.map { storiesDomainToUiMapper.map(it) }

        val storiesAdapterModels: MutableList<Item> =
            storiesToUserStoriesMapper(stories).map {
                userStoriesToStoriesItemMapper(listener = storiesOnClickListener, stories = it)
            }.toMutableList()

        storiesAdapterModels.add(0, addStories)
        val storiesItems = MainScreenStoriesBlockItem(storiesAdapterModels)
        allAdapterItems.addAll(listOf(storiesItems))

        val collectionsItem = MainScreenCollectionsBlockItem(allCollections)
        allAdapterItems.addAll(listOf(collectionsItem))

        val exclusiveAudioBooksHorizontal = ExclusiveAudioBookHorizontalItem(exclusiveAudioBooks)
        if (exclusiveAudioBooks.isNotEmpty()) allAdapterItems.add(
            HeaderItem(IdResourceString(R.string.exclusive_audio_books), false)
        )
        allAdapterItems.addAll(listOf(exclusiveAudioBooksHorizontal))

        val popularGenreItems = mutableListOf<Item>()
        listOf(limitedAudioBooks, filteredBooksList)
        popularGenreItems.addAll(limitedAudioBooks.shuffled())
        popularGenreItems.addAll(limitedBooks.shuffled())

        val booksHorizontalItem = BookHorizontalItem(limitedBooks)
        if (booksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllBooks { openMoreClickListeners.navigateToAllBooksFragment() })
        allAdapterItems.addAll(listOf(booksHorizontalItem))

        val addNewBooks = AddNewBooksItem(addNewBooksItemOnClickListener)
        allAdapterItems.addAll(listOf(addNewBooks))

        val savedHorizontalItem = HorizontalItem(savedFilteredBooksList)
        if (savedHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForSavedBooks { openMoreClickListeners.navigateToAllSavedBooksFragment() })
        allAdapterItems.addAll(listOf(savedHorizontalItem))

        val popularGenreBlockItem = PopularGenreBlockItem(items = popularGenreItems.shuffled())
        allAdapterItems.addAll(listOf(popularGenreBlockItem))

        val audioBooksHorizontalItem = MainScreenAudioBookBlockItem(limitedAudioBooks)
        if (audioBooksHorizontalItem.items.isNotEmpty()) allAdapterItems.add(
            createHeaderModelForAllAudioBooks { openMoreClickListeners.navigateToAllAudioBooksFragment() })
        allAdapterItems.addAll(listOf(audioBooksHorizontalItem))

//        val tasksHorizontal = HorizontalItemSecond(tasks)
//        if (tasksHorizontal.items.isNotEmpty()) allAdapterItems.add(
//            createHeaderModelForAllTasks { openMoreClickListeners.navigateToAllTasksFragment() })
//        allAdapterItems.addAll(listOf(tasksHorizontal))

        val selectFavoriteBooks = SelectFavoriteBooksItem(selectFavoriteBooksItemOnClickListener)
        allAdapterItems.addAll(listOf(selectFavoriteBooks))

        val exclusive = ExclusiveBookHorizontalItem(exclusiveBooks.swapElements())
        if (exclusive.items.isNotEmpty()) allAdapterItems.add(
            HeaderItem(IdResourceString(R.string.exclusive_books), false)
        )
        allAdapterItems.addAll(listOf(exclusive))

        val allGenreBlockItems = mutableListOf<Item>()
        if (genres.isNotEmpty()) allGenreBlockItems.add(
            HeaderItem(IdResourceString(R.string.genres), false)
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

    private fun createAddStoriesAdapterItem(
        listener: StoriesItemOnClickListener,
    ) = StoriesAddAdapterItem(listener = listener)

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