package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_details

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Status
import com.example.domain.models.book.AddNewBookRequest
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.usecase.AddNewStudentBookUseCase
import com.example.domain.usecase.GetMyBookUseCase
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentStudentBookDetailsViewModel @Inject constructor(
    private val addNewStudentBookUseCase: AddNewStudentBookUseCase,
    private val getMyBookUseCase: GetMyBookUseCase,
) : BaseViewModel() {

    private val _addBookState =
        MutableSharedFlow<PostRequestAnswer>(replay = 1, extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val addBookState: SharedFlow<PostRequestAnswer> get() = _addBookState.asSharedFlow()

    fun chekIsMyBook(userId: String, bookId: String) = liveData {
        getMyBookUseCase.execute(userId = userId, bookId = bookId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if (resource.data!!.isNotEmpty()) {
                        emit(resource.data!![0])
                        showProgressAnimation()
                    } else dismissProgressAnimation()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    goBack()
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    goBack()
                }
            }
        }
    }

    fun addNewBook(book: AddNewBookRequest) {
        addNewStudentBookUseCase.execute(book = book).onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    _addBookState.emit(resource.data!!)
                }
                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    goBack()
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun goBack() = navigateBack()

}

