package com.example.bookloverfinalapp.app.ui.admin_screens.screen_school_progress

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.communication.StudentsCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetAllBooksUseCase
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.interactor.GetClassStudentsUseCase
import com.example.domain.interactor.GetStudentBooksUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.ClassDomain
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentSchoolProgressViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getClassStudentsUseCase: GetClassStudentsUseCase,
    private val getAllClassUseCase: GetAllClassUseCase,
    private val getStudentBooksUseCase: GetStudentBooksUseCase,
    private val studentsCommunication: StudentsCommunication,
    private val classesCommunication: ClassesCommunication,
    private val bookCommunication: BooksThatReadCommunication,
    private val studentMapper: Mapper<StudentDomain, Student>,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
) : BaseViewModel() {

//    GetMyStudentsUseCase

    private val _schoolProgress =
        MutableSharedFlow<SchoolProgress>(replay = 1,
            extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val schoolProgress: SharedFlow<SchoolProgress> get() = _schoolProgress.asSharedFlow()

    fun classesObserve(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        classesCommunication.observe(owner = owner, observer = observer)

    fun studentObserve(owner: LifecycleOwner, observer: Observer<List<Student>>) =
        studentsCommunication.observe(owner = owner, observer = observer)

    fun booksObserve(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchAllBooks(schoolId: String) = dispatchers.launchInBackground(viewModelScope) {
        getAllBooksUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> fetchClasses(allBooks = resource.data!!.size, schoolId = schoolId)
                Status.EMPTY -> fetchClasses(allBooks = 0, schoolId = schoolId)
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }

    private fun fetchClasses(allBooks: Int, schoolId: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getAllClassUseCase.execute(schoolId = schoolId).collectLatest { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val classes = resource.data!!
                        classesCommunication.put(classes.map { classDomain ->
                            classMapper.map(classDomain)
                        })
                        fetchStudents(allBooks = allBooks.toString(), list = classes)
                    }
                    Status.EMPTY -> emptyResource(allBooks = allBooks.toString())
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }
                }
            }
        }

    private fun fetchStudents(allBooks: String, list: List<ClassDomain>) =
        dispatchers.launchInBackground(viewModelScope) {
            var chaptersRead = 0
            var allStudents = 0
            var booksRead = 0
            var pageRead = 0
            list.forEach { classDomain ->
                getClassStudentsUseCase.execute(classId = classDomain.id)
                    .collectLatest { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                allStudents += resource.data!!.size
                                resource.data!!.forEach { studentDomain ->
                                    chaptersRead += studentDomain.chaptersRead
                                    booksRead += studentDomain.booksRead
                                    pageRead += studentDomain.progress
                                }
                            }
                            Status.EMPTY -> emptyResource(allBooks)
                            Status.ERROR -> {
                                error(message = resource.message!!)
                                dismissProgressDialog()
                            }
                        }

                    }
            }
            _schoolProgress.emit(SchoolProgress(allBooks = allBooks,
                pageRead = pageRead.toString(),
                allStudents = allStudents.toString(),
                chaptersRead = chaptersRead.toString(),
                booksRead = booksRead.toString()))

        }

    fun fetchMyStudents(classId: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getClassStudentsUseCase.execute(classId = classId)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.LOADING -> showProgressDialog()
                        Status.SUCCESS -> {
                            studentsCommunication.put(resource.data!!.map { studentDomain ->
                                studentMapper.map(studentDomain)
                            })
                            dismissProgressDialog()
                        }
                        Status.EMPTY -> {
                            val emptyList = mutableListOf<Student>()
                            studentsCommunication.put(emptyList)
                            dismissProgressDialog()
                        }
                        Status.ERROR -> {
                            error(message = resource.message!!)
                            dismissProgressDialog()
                        }


                    }
                }
        }

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        getStudentBooksUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    bookCommunication.put(resource.data!!.map { studentBookDomain ->
                        bookMapper.map(studentBookDomain)
                    })
                    Log.i("dddd", resource.data!!.size.toString())
                    dismissProgressDialog()
                }
                Status.EMPTY -> dismissProgressDialog()
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()

                }
            }
        }
    }

    data class SchoolProgress(
        val allStudents: String,
        val allBooks: String,
        val chaptersRead: String,
        val booksRead: String,
        val pageRead: String,
    )

    private fun emptyResource(allBooks: String) = dispatchers.launchInBackground(viewModelScope) {
        _schoolProgress.emit(SchoolProgress(allBooks = allBooks,
            pageRead = 0.toString(),
            allStudents = 0.toString(),
            chaptersRead = 0.toString(),
            booksRead = 0.toString()))
        dismissProgressDialog()
    }
}

