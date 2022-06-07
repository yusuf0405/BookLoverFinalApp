package com.example.bookloverfinalapp.app.ui.general_screens.screen_class_has_deleted

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.interactor.UpdateStudentClassUseCase
import com.example.domain.models.ClassDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ClassHasDeletedViewModel @Inject constructor(
    private val getAllClassUseCase: GetAllClassUseCase,
    private val updateStudentClassUseCase: UpdateStudentClassUseCase,
    private val communication: ClassesCommunication,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        communication.observe(owner = owner, observer = observer)

    fun updateStudentClass(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ) = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        updateStudentClassUseCase.execute(id = id,
            sessionToken = sessionToken,
            classId = classId,
            classTitle = classTitle).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
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

    fun fetchClasses(schoolId: String) = launchInBackground {
        getAllClassUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    communication.put(resource.data!!.map { classDomain ->
                        classMapper.map(classDomain)
                    })
                }
                Status.EMPTY -> {}
                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
                }
            }
        }
    }

}