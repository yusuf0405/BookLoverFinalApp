package com.example.bookloverfinalapp.app.ui.screen_sign_up

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.ui.screen_sign_up_student.FragmentSignUpStudentDirections
import com.example.bookloverfinalapp.app.ui.screen_sign_up_teacher.FragmentSignUpTeacherDirections
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.interactor.SignUpUseCase
import com.example.domain.Status
import com.example.domain.models.ClassDomain
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain
import com.example.domain.interactor.GetAllSchoolsUseCase
import com.example.domain.interactor.GetClassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSignUpViewModel @Inject constructor(
    getAllSchoolsUseCase: GetAllSchoolsUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getClassUseCase: GetClassUseCase,
) : BaseViewModel() {

    private val _schools = MutableSharedFlow<List<SchoolDomain>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val schools: SharedFlow<List<SchoolDomain>> get() = _schools.asSharedFlow()

    private val _classes = MutableSharedFlow<List<ClassDomain>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val classes: SharedFlow<List<ClassDomain>> get() = _classes.asSharedFlow()

    init {
        getAllSchoolsUseCase.execute().onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> _schools.emit(resource.data!!)
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun signUp(user: UserSignUpDomain) = liveData(context = viewModelScope.coroutineContext) {
        signUpUseCase.execute(user = user).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    emit(resource.data!!)
                    dismissProgressDialog()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }

    fun getClasses(classesIds: List<String>) = viewModelScope.launch {
        val listClasses = mutableListOf<ClassDomain>()
        classesIds.forEach { id ->
            getClassUseCase.execute(id = id).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        listClasses.add(resource.data!![0])
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }
                }
            }
        }
        _classes.emit(listClasses)
    }

    fun goOverLoginFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())

    fun goStudentToLoginFragment() =
        navigate(FragmentSignUpStudentDirections.actionFragmentSignUpStudentToFragmentLogin())

    fun goTeacherToLoginFragment() =
        navigate(FragmentSignUpTeacherDirections.actionFragmentSignUpTeacherToFragmentLogin())


    fun goBack() = navigateBack()

}