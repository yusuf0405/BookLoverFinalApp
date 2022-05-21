package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.ClassAdapterModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root.FragmentAdminMainRootDirections
import com.example.bookloverfinalapp.app.utils.communication.ClassAdapterCommunication
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddNewClassUseCase
import com.example.domain.interactor.DeleteClassUseCase
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.models.ClassDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentClassesViewModel @Inject constructor(
    private val getAllClassUseCase: GetAllClassUseCase,
    private val deleteClassUseCase: DeleteClassUseCase,
    private var addNewClassUseCase: AddNewClassUseCase,
    private var dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<ClassDomain, ClassAdapterModel.Base>,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
    private val communication: ClassAdapterCommunication,
    private val classCommunication: ClassesCommunication,
) : BaseViewModel() {


    fun observe(owner: LifecycleOwner, observer: Observer<List<ClassAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun classObserve(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        classCommunication.observe(owner = owner, observer = observer)


    fun fetchClasses(schoolId: String) = dispatchers.launchInBackground(viewModelScope) {
        getAllClassUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.put(listOf(ClassAdapterModel.Progress))
                Status.SUCCESS -> {
                    val classListDomain = resource.data!!
                    classCommunication.put(classListDomain.map { classDomain ->
                        classMapper.map(classDomain)
                    })
                    communication.put(classListDomain.map { classDomain ->
                        mapper.map(classDomain)
                    })
                }
                Status.EMPTY -> communication.put(listOf(ClassAdapterModel.Empty))
                Status.ERROR -> communication.put(listOf(ClassAdapterModel.Fail(resource.message!!)))
            }
        }
    }

    fun deleteClass(id: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            deleteClassUseCase.execute(id = id).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
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


    fun addNewClass(title: String, schoolId: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            addNewClassUseCase.execute(title = title, schoolId = schoolId)
                .collectLatest { resource ->
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

    fun goClassStudentFragment(schoolClass: SchoolClass) =
        navigate(FragmentAdminMainRootDirections.actionFragmentAdminMainRootToFragmentClassStudents(
            schoolClass))
}