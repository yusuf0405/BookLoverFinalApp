package com.example.bookloverfinalapp.app.ui.admin_screens.screen_school_progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.communication.SchoolProgressCommunication
import com.example.bookloverfinalapp.app.utils.communication.StudentsCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetAllBooksUseCase
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.interactor.GetClassUsersUseCase
import com.example.domain.interactor.GetUsersBooksUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.ClassDomain
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentSchoolProgressViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getClassUsersUseCase: GetClassUsersUseCase,
    private val getAllClassUseCase: GetAllClassUseCase,
    private val getUsersBooksUseCase: GetUsersBooksUseCase,
    private val studentsCommunication: StudentsCommunication,
    private val schoolProgressCommunication: SchoolProgressCommunication,
    private val classesCommunication: ClassesCommunication,
    private val bookCommunication: BooksThatReadCommunication,
    private val studentMapper: Mapper<StudentDomain, Student>,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
) : BaseViewModel() {

    fun classesCollect(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        classesCommunication.observe(owner = owner, observer = observer)

    fun schoolProgressCollect(owner: LifecycleOwner, observer: Observer<SchoolProgress>) =
        schoolProgressCommunication.observe(owner = owner, observer = observer)

    fun studentsCollect(owner: LifecycleOwner, observer: Observer<List<Student>>) =
        studentsCommunication.observe(owner = owner, observer = observer)

    fun booksThatReadCollect(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchAllBooks(schoolId: String) = launchInBackground {
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

    private fun fetchClasses(allBooks: Int, schoolId: String) = launchInBackground {
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

    private fun fetchStudents(allBooks: String, list: List<ClassDomain>) = launchInBackground {
        var chaptersRead = 0
        var allStudents = 0
        var booksRead = 0
        var pageRead = 0
        list.forEach { classDomain ->
            getClassUsersUseCase.execute(classId = classDomain.id)
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
        schoolProgressCommunication.put(SchoolProgress(allBooks = allBooks,
            pageRead = pageRead.toString(),
            allStudents = allStudents.toString(),
            chaptersRead = chaptersRead.toString(),
            booksRead = booksRead.toString()))

    }

    fun fetchMyStudents(classId: String) = launchInBackground {
        getClassUsersUseCase.execute(classId = classId)
            .collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        studentsCommunication.put(resource.data!!.map { studentDomain ->
                            studentMapper.map(studentDomain)
                        })
                    }
                    Status.EMPTY -> {
                        dismissProgressDialog()
                        val emptyList = mutableListOf<Student>()
                        studentsCommunication.put(emptyList)
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }


                }
            }
    }

    fun fetchMyBook(id: String) = launchInBackground {
        getUsersBooksUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    bookCommunication.put(resource.data!!.map { studentBookDomain ->
                        bookMapper.map(studentBookDomain)
                    })
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

    private fun emptyResource(allBooks: String) = launchInBackground {
        dismissProgressDialog()
        schoolProgressCommunication.put(SchoolProgress(allBooks = allBooks,
            pageRead = 0.toString(),
            allStudents = 0.toString(),
            chaptersRead = 0.toString(),
            booksRead = 0.toString()))
    }
}

