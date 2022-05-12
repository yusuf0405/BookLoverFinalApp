package com.example.bookloverfinalapp.app.ui.screen_progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.bookloverfinalapp.app.utils.communication.StudentsCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetBookThatReadUseCase
import com.example.domain.interactor.GetMyStudentsUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentProgressViewModel @Inject constructor(
    private val bookCommunication: BooksThatReadCommunication,
    private val studentCommunication: StudentsCommunication,
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val getMyStudentsUseCase: GetMyStudentsUseCase,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val studentMapper: Mapper<StudentDomain, Student>,
) : BaseViewModel() {

    fun booksObserve(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun studentsObserve(owner: LifecycleOwner, observer: Observer<List<Student>>) =
        studentCommunication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        bookThatReadUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressAnimation()
                Status.SUCCESS -> {
                    dismissProgressAnimation()
                    bookCommunication.put(resource.data!!.map { studentBookDomain ->
                        bookMapper.map(studentBookDomain)
                    })
                }
                Status.EMPTY -> dismissProgressAnimation()
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressAnimation()
                }
            }
        }
    }

    fun fetchMyStudent(classId: String, id: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getMyStudentsUseCase.execute(classId = classId).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressAnimation()
                    Status.SUCCESS -> {
                        studentCommunication.put(resource.data!!.map { studentDomain ->
                            studentMapper.map(studentDomain)
                        })
                        fetchMyBook(id = id)
                    }
                    Status.EMPTY -> dismissProgressAnimation()
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressAnimation()
                    }
                }
            }
        }
}