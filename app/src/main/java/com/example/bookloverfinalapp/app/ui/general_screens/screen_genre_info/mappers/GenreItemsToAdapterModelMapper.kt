package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.mappers

import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models.HorizontalBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BookHorizontalItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenAudioBookBlockItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.AudioBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.AudioBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.example.data.cache.models.IdResourceString
import com.example.domain.Mapper
import com.example.domain.models.AudioBookDomain
import com.example.domain.models.BookDomain
import javax.inject.Inject

interface GenreItemsToAdapterModelMapper {

    fun map(
        items: Pair<List<BookDomain>, List<AudioBookDomain>>,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        bookOnClickListeners: BookItemOnClickListener,
        searchQuery: String
    ): MutableList<Item>
}

class GenreItemsToAdapterModelMapperImpl @Inject constructor(
    private val bookDomainToBookMapper: Mapper<BookDomain, Book>,
    private val audioBookDomainToUiMapper: Mapper<AudioBookDomain, AudioBook>,
) : GenreItemsToAdapterModelMapper {

    override fun map(
        items: Pair<List<BookDomain>, List<AudioBookDomain>>,
        audioBookItemOnClickListener: AudioBookItemOnClickListener,
        bookOnClickListeners: BookItemOnClickListener,
        searchQuery: String
    ): MutableList<Item> {
        val allAdapterItems = mutableListOf<Item>()

        val books = items.first.map(bookDomainToBookMapper::map)
            .filter { applyFilterForAllBooks(it, searchQuery) }
            .map { createHorizontalItem(it, bookOnClickListeners) }

        val horizontalBooks = BookHorizontalItem(books)

        val filteredAudioBooksList = items.second
            .filter { applyFilterForAllAudioBooks(it, searchQuery) }
            .map(audioBookDomainToUiMapper::map)
            .map { AudioBookAdapterModel(it, audioBookItemOnClickListener) }

        if (horizontalBooks.items.isNotEmpty()) {
            allAdapterItems.add(createHeaderModelForAllBooks())
        }
        allAdapterItems.add(horizontalBooks)
        val audioBooksHorizontalItem = MainScreenAudioBookBlockItem(filteredAudioBooksList)
        if (filteredAudioBooksList.isNotEmpty()) {
            allAdapterItems.add(createHeaderModelForAllAudioBooks())
        }
        allAdapterItems.addAll(listOf(audioBooksHorizontalItem))

        return allAdapterItems
    }

    private fun createHorizontalItem(
        book: Book,
        bookOnClickListeners: BookItemOnClickListener,
    ) = HorizontalBookAdapterModel(
        id = book.id,
        title = book.title,
        author = book.author,
        description = book.description,
        savedStatus = book.savedStatus,
        posterUrl = book.poster.url,
        listener = bookOnClickListeners
    )


    private fun createHeaderModelForAllBooks() =
        HeaderItem(
            titleId = IdResourceString(R.string.books),
            onClickListener = { },
            showMoreIsVisible = false
        )

    private fun createHeaderModelForAllAudioBooks() =
        HeaderItem(
            titleId = IdResourceString(R.string.audio_books),
            onClickListener = { },
            showMoreIsVisible = false
        )

    private fun applyFilterForAllAudioBooks(book: AudioBookDomain, searchQuery: String) =
        if (searchQuery.isEmpty()) book.title != String()
        else book.title.contains(searchQuery, ignoreCase = true)

    private fun applyFilterForAllBooks(book: Book, searchQuery: String) =
        if (searchQuery.isEmpty()) book.title != String()
        else book.title.contains(searchQuery, ignoreCase = true)

}