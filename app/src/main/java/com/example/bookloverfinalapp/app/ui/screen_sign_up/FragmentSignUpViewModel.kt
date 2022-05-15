package com.example.bookloverfinalapp.app.ui.screen_sign_up

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.screen_sign_up_student.FragmentSignUpStudentDirections
import com.example.bookloverfinalapp.app.ui.screen_sign_up_teacher.FragmentSignUpTeacherDirections
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddSessionTokenUseCase
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.interactor.GetAllSchoolsUseCase
import com.example.domain.interactor.SignUpUseCase
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSignUpViewModel @Inject constructor(
    private val getAllSchoolsUseCase: GetAllSchoolsUseCase,
    private val addSessionTokenUseCase: AddSessionTokenUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getAllClassUseCase: GetAllClassUseCase,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
) : BaseViewModel() {

    private val _schools =
        MutableSharedFlow<List<SchoolDomain>>(replay = 1, extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val schools: SharedFlow<List<SchoolDomain>> get() = _schools.asSharedFlow()

    private val _classes = MutableSharedFlow<List<SchoolClass>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val classes: SharedFlow<List<SchoolClass>> get() = _classes.asSharedFlow()

    private val _schoolError = MutableSharedFlow<Event<String>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val schoolError: SharedFlow<Event<String>> get() = _schoolError.asSharedFlow()

    private val _classError = MutableSharedFlow<Event<String>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val classError: SharedFlow<Event<String>> get() = _classError.asSharedFlow()

    init {
        getAllSchools()
    }

    fun getAllSchools() {
        getAllSchoolsUseCase.execute().onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> _schools.emit(resource.data!!)
                Status.ERROR -> {
                    dismissProgressDialog()
                    _schoolError.emit(Event(resource.message!!))
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun signUp(user: UserSignUpDomain) = liveData(context = viewModelScope.coroutineContext) {
        signUpUseCase.execute(user = user).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> emit(resource.data!!)
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }

    fun addSessionToken(id: String, sessionToken: String) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            addSessionTokenUseCase.execute(id = id, sessionToken = sessionToken)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            emit(Unit)
                            dismissProgressDialog()
                        }
                        Status.ERROR -> {
                            error(message = resource.message!!)
                            dismissProgressDialog()
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
                    _classes.emit(resource.data!!.map { classDomain -> classMapper.map(classDomain) })
                }
                Status.ERROR -> {
                    dismissProgressDialog()
                    _schoolError.emit(Event(resource.message!!))
                }
            }
        }

    }

    fun goOverLoginFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())

    fun goStudentToLoginFragment() =
        navigate(FragmentSignUpStudentDirections.actionFragmentSignUpStudentToFragmentLogin())

    fun goTeacherToLoginFragment() =
        navigate(FragmentSignUpTeacherDirections.actionFragmentSignUpTeacherToFragmentLogin())


    fun goBack() = navigateBack()

}