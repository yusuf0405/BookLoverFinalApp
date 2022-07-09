package com.example.bookloverfinalapp.app.ui.general_screens.screen_my_students

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.StudentDomainToUserModelMapper
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.CatEmptyModel
import com.example.bookloverfinalapp.app.models.ErrorModel
import com.example.bookloverfinalapp.app.models.UserLoadingModel
import com.example.bookloverfinalapp.app.models.UserModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_VIEW_HOLDER
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.GetMyStudentsUseCase
import com.example.domain.interactor.StudentsOnRefreshUseCase
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentMyStudentsViewModel @Inject constructor(
    private val getMyStudentsUseCase: GetMyStudentsUseCase,
    private val onRefreshUseCase: StudentsOnRefreshUseCase,
    private val communication: ItemUiCommunication,
    val adapterMapper: Mapper<UserModel, Student>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)


    fun fetchMyStudents(classId: String) = launchInBackground {
        getMyStudentsUseCase.execute(classId = classId).collectLatest { resource ->
            unravelingResource(resource = resource, classId = classId)
        }
    }

    fun onRefresh(classId: String) = launchInBackground {
        onRefreshUseCase.execute(classId = classId).collectLatest { resource ->
            unravelingResource(resource = resource, classId = classId)
        }
    }

    private fun unravelingResource(resource: Resource<List<StudentDomain>>, classId: String) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(UserLoadingModel))
            Status.SUCCESS -> communication.put(resource.data!!.map { studentDomain ->
                StudentDomainToUserModelMapper(ADAPTER_USER_VIEW_HOLDER).map(
                    studentDomain)
            })
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!
            ) { onRefresh(classId = classId) }))
        }
    }

    fun goStudentDetailsFragment(student: Student) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootBookToFragmentStudentDetails(
            student = student))
}