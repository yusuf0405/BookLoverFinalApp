package com.example.bookloverfinalapp.app.ui.admin_screens.screen_class_students

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.bookloverfinalapp.app.utils.communication.StudentCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetClassStudentsUseCase
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentClassStudentsViewModel @Inject constructor(
    private val getClassStudentsUseCase: GetClassStudentsUseCase,
    private val communication: StudentCommunication,
    private val mapper: Mapper<StudentDomain, StudentAdapterModel.Base>,
) : BaseViewModel() {


    fun observe(owner: LifecycleOwner, observer: Observer<List<StudentAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun goStudentDetailsFragment(student: Student) =
        navigate(FragmentClassStudentsDirections.actionFragmentClassStudentsToFragmentStudentDetails2(
            student = student))

    fun fetchMyStudents(classId: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getClassStudentsUseCase.execute(classId = classId)
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

    fun goBack() = navigateBack()
}