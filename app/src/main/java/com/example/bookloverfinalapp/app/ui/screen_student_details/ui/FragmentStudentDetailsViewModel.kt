package com.example.bookloverfinalapp.app.ui.screen_student_details.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadAdapterCommunication
import com.example.domain.Mapper
import com.example.domain.interactor.GetMyStudentBooksUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentStudentDetailsViewModel @Inject constructor(
    private val myStudentBooksUseCase: GetMyStudentBooksUseCase,
    private val communication: BooksThatReadAdapterCommunication,
    private val mapper: Mapper<BookThatReadDomain, BookThatReadAdapterModel.Base>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookThatReadAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        myStudentBooksUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.put(listOf(BookThatReadAdapterModel.Progress))
                Status.SUCCESS -> communication.put(resource.data!!.map { studentBookDomain -> mapper.map(studentBookDomain) })
                Status.EMPTY -> communication.put(listOf(BookThatReadAdapterModel.Empty))
                Status.ERROR -> communication.put(listOf(BookThatReadAdapterModel.Fail(resource.message!!)))

            }
        }
    }

    fun goBack() = navigateBack()
}