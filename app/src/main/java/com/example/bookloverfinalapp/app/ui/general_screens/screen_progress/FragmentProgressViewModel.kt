package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.StudentDomainToUserModelMapper
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.adapter.CubeEmptyModel
import com.example.bookloverfinalapp.app.ui.adapter.ErrorModel
import com.example.bookloverfinalapp.app.ui.adapter.UserLoadingModel
import com.example.bookloverfinalapp.app.ui.adapter.UserModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_RATING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.bookloverfinalapp.app.utils.communication.ClassStatisticsCommunication
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.GetBookThatReadUseCase
import com.example.domain.interactor.GetMyStudentsUseCase
import com.example.domain.interactor.GetSchoolStudentsUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentProgressViewModel @Inject constructor(
    private val bookCommunication: BooksThatReadCommunication,
    private val studentCommunication: ItemUiCommunication,
    private val classStatisticsCommunication: ClassStatisticsCommunication,
    private val getSchoolStudentsUseCase: GetSchoolStudentsUseCase,
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val getMyStudentsUseCase: GetMyStudentsUseCase,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    val adapterMapper: Mapper<UserModel, Student>,
) : BaseViewModel() {

    fun booksThatReadCollect(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun studentAdapterModelsCollect(
        owner: LifecycleOwner,
        observer: Observer<List<ItemUi>>,
    ) = studentCommunication.observe(owner = owner, observer = observer)

    fun statisticsObserve(owner: LifecycleOwner, observer: Observer<ClassStatistics>) =
        classStatisticsCommunication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = launchInBackground {
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

    fun fetchSchoolStudents(schoolId: String) = launchInBackground {
        getSchoolStudentsUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            unravelingSchoolsStudents(resource = resource, schoolId = schoolId)
        }
    }

    private fun unravelingSchoolsStudents(
        resource: Resource<List<StudentDomain>>,
        schoolId: String,
    ) {
        when (resource.status) {
            Status.LOADING -> studentCommunication.put(listOf(UserLoadingModel))
            Status.SUCCESS -> studentCommunication.put(resource.data!!.sortedByDescending { it.progress }
                .map { student ->
                    StudentDomainToUserModelMapper(ADAPTER_USER_RATING_VIEW_HOLDER).map(student)
                })
            Status.EMPTY -> studentCommunication.put(listOf(CubeEmptyModel))
            Status.ERROR -> studentCommunication.put(listOf(ErrorModel(resource.message!!
            ) { fetchSchoolStudents(schoolId = schoolId) }))
        }
    }


    fun fetchMyStudent(classId: String) = launchInBackground {
        getMyStudentsUseCase.execute(classId = classId).collectLatest { resource ->
            unravelingClassStudents(resource = resource, classId = classId)
        }
    }

    private fun unravelingClassStudents(resource: Resource<List<StudentDomain>>, classId: String) {
        when (resource.status) {
            Status.LOADING -> {
                studentCommunication.put(listOf(UserLoadingModel))
                showProgressAnimation()
            }
            Status.SUCCESS -> {
                var readPages = 0
                var readChapters = 0
                var readBooks = 0
                val students = resource.data!!
                students.forEach { student ->
                    readPages += student.progress
                    readChapters += student.chaptersRead
                    readBooks += student.booksRead
                }
                classStatisticsCommunication.put(ClassStatistics(readPages,
                    readChapters,
                    readBooks))

                studentCommunication.put(resource.data!!.sortedByDescending { it.progress }
                    .map { studentDomain ->
                        StudentDomainToUserModelMapper(ADAPTER_USER_RATING_VIEW_HOLDER).map(
                            studentDomain)
                    })
                dismissProgressAnimation()

            }
            Status.EMPTY -> {
                dismissProgressAnimation()
                studentCommunication.put(listOf(CubeEmptyModel))
            }
            Status.ERROR -> {
                studentCommunication.put(listOf(ErrorModel(resource.message!!
                ) { fetchMyStudent(classId) }))
                error(message = resource.message!!)
                dismissProgressAnimation()
            }
        }

    }


    data class ClassStatistics(
        var readPages: Int,
        var readChapters: Int,
        var readBooks: Int,
    )

    fun goStudentDetailsFragment(student: Student) =
        navigate(FragmentProgressDirections.actionFragmentProgressToFragmentStudentDetails(student = student))
}