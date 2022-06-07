package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up_student.FragmentSignUpStudentDirections
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up_teacher.FragmentSignUpTeacherDirections
import com.example.bookloverfinalapp.app.utils.communication.ClassErrorCommunication
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.communication.SchoolsCommunication
import com.example.bookloverfinalapp.app.utils.communication.SchoolsErrorCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddSessionTokenUseCase
import com.example.domain.interactor.GetAllClassesCloudUseCase
import com.example.domain.interactor.GetAllSchoolsUseCase
import com.example.domain.interactor.SignUpUseCase
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSignUpViewModel @Inject constructor(
    private val getAllSchoolsUseCase: GetAllSchoolsUseCase,
    private val addSessionTokenUseCase: AddSessionTokenUseCase,
    private val getAllClassUseCase: GetAllClassesCloudUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val schoolsCommunication: SchoolsCommunication,
    private val classesCommunication: ClassesCommunication,
    private val schoolsErrorCommunication: SchoolsErrorCommunication,
    private val classErrorCommunication: ClassErrorCommunication,
    private val dispatchersProvider: DispatchersProvider,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
) : BaseViewModel() {

    init {
        getAllSchools()
    }

    fun schoolsCollect(owner: LifecycleOwner, observer: Observer<List<SchoolDomain>>) =
        schoolsCommunication.observe(owner = owner, observer = observer)

    fun classesCollect(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        classesCommunication.observe(owner = owner, observer = observer)

    fun schoolsErrorCollect(owner: LifecycleOwner, observer: Observer<Event<String>>) =
        schoolsErrorCommunication.observe(owner = owner, observer = observer)

    fun classErrorCollect(owner: LifecycleOwner, observer: Observer<Event<String>>) =
        classErrorCommunication.observe(owner = owner, observer = observer)

    fun getAllSchools() {
        getAllSchoolsUseCase.execute().onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> schoolsCommunication.put(resource.data!!)
                Status.ERROR -> {
                    dismissProgressDialog()
                    schoolsErrorCommunication.put(Event(resource.message!!))
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun signUp(user: UserSignUpDomain) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            signUpUseCase.execute(user = user).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> emit(resource.data!!)
                    Status.ERROR -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                    }
                }
            }
        }

    fun addSessionToken(id: String, sessionToken: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            addSessionTokenUseCase.execute(id = id, sessionToken = sessionToken)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            dismissProgressDialog()
                            emit(Unit)
                        }
                        Status.ERROR -> {
                            dismissProgressDialog()
                            error(message = resource.message!!)
                        }
                    }
                }
        }

    fun getClasses(schoolId: String) = viewModelScope.launch {
        getAllClassUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    classesCommunication.put(resource.data!!.map { classDomain ->
                        classMapper.map(classDomain)
                    })
                }
                Status.ERROR -> {
                    dismissProgressDialog()
                    schoolsErrorCommunication.put(Event(resource.message!!))
                }
            }
        }
    }

    fun goStudentToLoginFragment() =
        navigate(FragmentSignUpStudentDirections.actionFragmentSignUpStudentToFragmentLogin())

    fun goTeacherToLoginFragment() =
        navigate(FragmentSignUpTeacherDirections.actionFragmentSignUpTeacherToFragmentLogin())


    fun goBack() = navigateBack()

}