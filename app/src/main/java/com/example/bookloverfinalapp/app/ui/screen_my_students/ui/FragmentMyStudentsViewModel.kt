package com.example.bookloverfinalapp.app.ui.screen_my_students.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.bookloverfinalapp.app.ui.screen_book_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.StudentCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.GetMyStudentsUseCase
import com.example.domain.domain.models.StudentDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentMyStudentsViewModel @Inject constructor(
    private val getMyStudentsUseCase: GetMyStudentsUseCase,
    private val communication: StudentCommunication,
    private val mapper: Mapper<StudentDomain, StudentAdapterModel.Base>,
    ) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<StudentAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun goStudentDetailsFragment(student: Student) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootBookToFragmentStudentDetails(
            student = student))

    fun fetchMyStudents(className: String, schoolName: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getMyStudentsUseCase.execute(className = className, schoolName = schoolName)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.LOADING -> communication.put(listOf(StudentAdapterModel.Progress))
                        Status.SUCCESS -> communication.put(resource.data!!.map { studentDomain ->
                            mapper.map(studentDomain)
                        })
                        Status.EMPTY -> communication.put(listOf(StudentAdapterModel.Empty))
                        Status.ERROR -> communication.put(listOf(StudentAdapterModel.Fail(resource.message!!)))

                    }
                }
        }


}