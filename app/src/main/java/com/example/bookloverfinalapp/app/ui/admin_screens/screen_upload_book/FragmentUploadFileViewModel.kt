package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddNewBook
import com.example.bookloverfinalapp.app.models.BookPdf
import com.example.bookloverfinalapp.app.models.BookPoster
import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.GenreDomain
import com.example.domain.models.UserDomain
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.GenresRepository
import com.example.domain.repository.UserCacheRepository
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ProgressCallback
import com.parse.SaveCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentUploadFileViewModel @Inject constructor(
    private val repository: BooksRepository,
    userCacheRepository: UserCacheRepository,
    private val genresRepository: GenresRepository,
    private val resourceProvider: ResourceProvider,
    private val dispatchersProvider: DispatchersProvider,
    private val newBookUiToDomainMapper: Mapper<AddNewBook, AddNewBookDomain>,
    private val genreDomainToUiMapper: Mapper<GenreDomain, Genre>
) : BaseViewModel(), ProgressCallback, SaveCallback {

    private val currentUser = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserDomain.unknown())

    private val bookPdfFlow = MutableStateFlow<ParseFile?>(null)
    private val bookPosterFlow = MutableStateFlow<ParseFile?>(null)
    private val bookPageCountFlow = MutableStateFlow(0)
    private val bookChapterCountFlow = MutableStateFlow(0)
    private val checkedBookGenresListFlow = MutableStateFlow<MutableList<Genre>>(mutableListOf())

    private var _motionPosition = MutableStateFlow(0f)
    val motionPosition get() = _motionPosition.asStateFlow()

    private val _startAddNewBookFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val startAddNewBookFlow get() = _startAddNewBookFlow.asSharedFlow()

    private val _fileUploadProgressFlow = createMutableSharedFlowAsLiveData<Int>()
    val fileUploadProgressFlow get() = _fileUploadProgressFlow.asSharedFlow()

    private val _fileUploadNotificationsShowingFlow =
        createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val fileUploadNotificationsShowingFlow get() = _fileUploadNotificationsShowingFlow.asSharedFlow()

    val allGenres = genresRepository.fetchAllGenres()
        .map { genres -> genres.map(genreDomainToUiMapper::map) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun startSaveNewBook() {
        _fileUploadNotificationsShowingFlow.tryEmit(true)
        bookPosterFlow.value!!.saveInBackground({ pdfException ->
            if (pdfException != null) {
                _fileUploadNotificationsShowingFlow.tryEmit(false)
                emitToErrorMessageFlow(IdResourceString(R.string.generic_error))
            } else bookPdfFlow.value?.saveInBackground(this, this)
        }, (this))

    }

    fun updateMotionPosition(position: Float) = _motionPosition.tryEmit(position)

    fun updateBookPosterFlow(file: ParseFile) = bookPosterFlow.tryEmit(file)

    fun addNewGenreToCheckedBookGenresList(genre: Genre) =
        checkedBookGenresListFlow.value.add(genre)

    fun removeNewGenreToCheckedBookGenresList(genre: Genre) =
        checkedBookGenresListFlow.value.remove(genre)

    fun updateBookPdfFlow(file: ParseFile) = bookPdfFlow.tryEmit(file)

    fun updateBookChapterCountFlow(count: Int) = bookChapterCountFlow.tryEmit(count)

    fun updateBookPageCountFlow(count: Int) = bookPageCountFlow.tryEmit(count)

    fun fetchCheckedBookGenresListValue() = checkedBookGenresListFlow.value

    fun addBook(
        title: String,
        description: String,
        author: String,
        publicYear: String,
        isExclusive: Boolean
    ) {
        val newBook = AddNewBook(
            author = author,
            description = description,
            chapterCount = bookChapterCountFlow.value,
            title = title,
            publicYear = publicYear,
            poster = parseFileMapToBookPoster(bookPosterFlow.value),
            book = parseFileMapToBookPdf(bookPdfFlow.value),
            page = bookPageCountFlow.value,
            schoolId = currentUser.value.schoolId,
            genres = checkedBookGenresListFlow.value.map { it.id },
            isExclusive = isExclusive
        )
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.addNewBook(newBookUiToDomainMapper.map(newBook)) },
            onSuccess = { handleAddBookSuccess() },
            onError = ::handleAddBookError
        )
    }

    private fun handleAddBookSuccess() {
        emitToShowSuccessNotificationFlow(IdResourceString(R.string.book_added_successfully))
    }

    private fun handleAddBookError(error: Throwable) {
        emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(error))
    }


    private fun parseFileMapToBookPdf(parseFile: ParseFile?) =
        if (parseFile == null) BookPdf.unknown()
        else BookPdf(name = parseFile.name, url = parseFile.url)

    private fun parseFileMapToBookPoster(parseFile: ParseFile?) =
        if (parseFile == null) BookPoster.unknown()
        else BookPoster(name = parseFile.name, url = parseFile.url)

    override fun done(percentDone: Int?) {
        percentDone?.apply(_fileUploadProgressFlow::tryEmit)
    }

    override fun done(posterException: ParseException?) {
        if (posterException != null) return
        _startAddNewBookFlow.tryEmit(Unit)
    }
}