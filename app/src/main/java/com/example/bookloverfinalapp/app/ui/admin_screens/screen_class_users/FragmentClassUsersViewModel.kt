package com.example.bookloverfinalapp.app.ui.admin_screens.screen_class_users

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.StudentDomainToUserModelMapper
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.adapter.*
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.GetClassUsersUseCase
import com.example.domain.models.StudentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentClassUsersViewModel @Inject constructor(
    private val getClassUsersUseCase: GetClassUsersUseCase,
    private val communication: ItemUiCommunication,
    val adapterMapper: Mapper<UserModel, Student>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchUsers(classId: String) = launchInBackground {
        getClassUsersUseCase.execute(classId = classId)
            .collectLatest { resource ->
                unravelingResource(resource = resource, classId = classId)
            }
    }

    private fun unravelingResource(
        resource: Resource<List<StudentDomain>>,
        classId: String,
    ) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(UserLoadingModel))
            Status.SUCCESS -> distributionUsers(resource.data!!.map { userDomain ->
                StudentDomainToUserModelMapper(ADAPTER_USER_VIEW_HOLDER).map(userDomain)
            })
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!
            ) { fetchUsers(classId = classId) }))


        }
    }

    private fun distributionUsers(users: List<UserModel>) {
        var isTeacher = true
        var isStudent = true
        val allUsers = mutableListOf<ItemUi>()
        val students = mutableListOf<ItemUi>()
        val teachers = mutableListOf<ItemUi>()
        users.forEach { user ->
            if (user.userType == UserType.student.name) {
                students.add(user)
                if (isStudent) isStudent = false
            }
            if (user.userType == UserType.teacher.name) {
                teachers.add(user)
                if (isTeacher) isTeacher = false
            }

        }
        if (!isTeacher) allUsers.add(UserTypeModel(userType = UserType.teacher))
        teachers.forEach { teacher -> allUsers.add(teacher) }
        if (!isStudent) allUsers.add(UserTypeModel(userType = UserType.student))
        students.forEach { student -> allUsers.add(student) }
        launchInBackground { communication.put(allUsers) }
    }

    fun goUserDetailsFragment(student: Student) =
        navigate(FragmentClassUsersDirections.actionFragmentClassStudentsToFragmentStudentDetails2(
            student = student))

    fun goBack() = navigateBack()
}