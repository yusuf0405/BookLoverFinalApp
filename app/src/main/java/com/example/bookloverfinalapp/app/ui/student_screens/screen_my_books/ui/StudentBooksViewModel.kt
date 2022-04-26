package com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel
import com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.StudentBooksAdapterCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.DeleteFromMyBooksUseCase
import com.example.domain.domain.interactor.GetStudentBookUseCase
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class StudentBooksViewModel @Inject constructor(
    private val studentBookUseCase: GetStudentBookUseCase,
    private val deleteFromMyBooksUseCase: DeleteFromMyBooksUseCase,
    private val communication: StudentBooksAdapterCommunication,
    private val mapper: Mapper<StudentBookDomain, StudentBookAdapterModel.Base>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<StudentBookAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun goChapterFragment(book: StudentBook) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootStudentBookToFragmentStudentChapterBook(
            book = book))

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        studentBookUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.map(listOf(StudentBookAdapterModel.Progress))
                Status.SUCCESS -> {
                    val bookList =
                        resource.data!!.map { studentBookDomain -> mapper.map(studentBookDomain) }
                    if (bookList.isEmpty()) communication.map(listOf(StudentBookAdapterModel.Empty))
                    else communication.map(bookList)
                }
                Status.ERROR ->
                    communication.map(listOf(StudentBookAdapterModel.Fail(resource.message!!)))

                Status.NETWORK_ERROR -> {}
            }
        }
    }

    fun deleteBook(id: String) = liveData(context = viewModelScope.coroutineContext) {
        deleteFromMyBooksUseCase.execute(id).flowOn(Dispatchers.IO).collectLatest { resource ->
            if (resource.status == Status.SUCCESS) emit(resource.data!!)
        }
    }

    fun listIsEmpty() = communication.map(listOf(StudentBookAdapterModel.Empty))

}