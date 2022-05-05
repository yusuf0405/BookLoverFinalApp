package com.example.bookloverfinalapp.app.ui.screen_sign_up

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.models.Status
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.UserSignUpRes
import com.example.domain.usecase.GetAllSchoolsUseCase
import com.example.domain.usecase.GetClassUseCase
import com.example.domain.usecase.SignUpUseCase
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

    private val _schools = MutableSharedFlow<List<School>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val schools: SharedFlow<List<School>> get() = _schools.asSharedFlow()

    private val _classes = MutableSharedFlow<List<Class>>(replay = 1, extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val classes: SharedFlow<List<Class>> get() = _classes.asSharedFlow()

    init {
        getAllSchoolsUseCase.execute().onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> _schools.emit(resource.data!!)
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    dismissProgressDialog()
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun signUp(user: UserSignUpRes) = liveData(context = viewModelScope.coroutineContext) {
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
                Status.NETWORK_ERROR -> {
                    networkError()
                    dismissProgressDialog()
                }
            }
        }
    }

    fun getClasses(classesIds: List<String>) = viewModelScope.launch {
        val listClasses = mutableListOf<Class>()
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
                    Status.NETWORK_ERROR -> {
                        networkError()
                        dismissProgressDialog()
                    }
                }
            }
        }
        _classes.emit(listClasses)
    }

    fun goOverLoginFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())

    fun goBack() = navigateBack()

}