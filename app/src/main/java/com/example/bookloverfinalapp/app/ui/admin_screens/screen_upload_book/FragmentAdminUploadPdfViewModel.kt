package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddNewBook
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddNewBookUseCase
import com.example.domain.models.AddNewBookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAdminUploadPdfViewModel @Inject constructor(
    private val addNewBookUseCase: AddNewBookUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<AddNewBook, AddNewBookDomain>,
) : BaseViewModel() {


    fun addBook(book: AddNewBook) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            addNewBookUseCase.execute(book = mapper.map(book)).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        emit(resource.data!!)
                        dismissProgressDialog()
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }
                }
            }
        }

}