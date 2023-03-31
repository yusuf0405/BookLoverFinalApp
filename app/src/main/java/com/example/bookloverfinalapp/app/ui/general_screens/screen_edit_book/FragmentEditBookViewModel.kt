package com.example.bookloverfinalapp.app.ui.general_screens.screen_edit_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.bookloverfinalapp.app.utils.extensions.toImage
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.models.UpdateBookDomain
import com.example.domain.repository.BooksRepository
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ProgressCallback
import com.parse.SaveCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentEditBookViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
) : BaseViewModel(), SaveCallback, ProgressCallback {


    private val _isShowLoadingDialog = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isShowLoadingDialog: SharedFlow<Boolean> get() = _isShowLoadingDialog.asSharedFlow()

    private val _currentUserImageFile = MutableStateFlow<ParseFile?>(null)
    val currentUserImageFile get() = _currentUserImageFile.asStateFlow()

    private val _imageUploadDialogIsShowFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val imageUploadDialogIsShowFlow get() = _imageUploadDialogIsShowFlow.asSharedFlow()

    private val _startUpdateUserFlow = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val startUpdateUserFlow get() = _startUpdateUserFlow.asSharedFlow()

    private val _currentUserImage = MutableStateFlow<UserImage?>(null)
    val currentUserImage get() = _currentUserImage.asStateFlow()

    private val _imageUploadDialogPercentFlow = createMutableSharedFlowAsSingleLiveEvent<Int>()
    val imageUploadDialogPercentFlow get() = _imageUploadDialogPercentFlow.asSharedFlow()

    fun updateBook(bookId: String, updateBook: UpdateBookDomain) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            onStart = { showProgressBar() },
            safeAction = {
                booksRepository.updateBook(id = bookId, book = updateBook)
            },
            onSuccess = {
                emitToShowSuccessNotificationFlow(IdResourceString(R.string.book_updated_successfully))
                dismissProgressBar()
            },
            onError = {
                dismissProgressBar()
                emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it))
            }
        )
    }

    fun checkParseFileAndStartUserUpdate() {
        if (currentUserImageFile.value == null) {
            _startUpdateUserFlow.tryEmit(Unit)
        } else {
            _imageUploadDialogIsShowFlow.tryEmit(true)
            saveImage()
        }
    }

    fun updateCurrentUserImageFile(file: ParseFile) = _currentUserImageFile.tryEmit(file)

    private fun saveImage() {
        currentUserImageFile.value?.apply {
            saveInBackground((this@FragmentEditBookViewModel), (this@FragmentEditBookViewModel))
        }
    }

    private fun showProgressBar() {
        _isShowLoadingDialog.tryEmit(true)
    }

    private fun dismissProgressBar() {
        _isShowLoadingDialog.tryEmit(false)
    }

    private fun updateCurrentUserImage() {
        currentUserImageFile.value?.apply { _currentUserImage.tryEmit(toImage()) }
    }

    override fun done(parseException: ParseException?) {
        if (parseException == null) {
            updateCurrentUserImage()
            _imageUploadDialogIsShowFlow.tryEmit(false)
            _startUpdateUserFlow.tryEmit(Unit)
        } else {
            emitToErrorMessageFlow(IdResourceString(R.string.generic_error))
        }
    }

    override fun done(percentDone: Int?) {
        if (percentDone == null) return
        _imageUploadDialogPercentFlow.tryEmit(percentDone)
    }
}