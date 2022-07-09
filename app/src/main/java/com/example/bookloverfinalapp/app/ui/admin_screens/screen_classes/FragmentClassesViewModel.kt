package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.*
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root.FragmentAdminMainRootDirections
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.AddNewClassUseCase
import com.example.domain.interactor.DeleteClassUseCase
import com.example.domain.interactor.GetAllClassUseCase
import com.example.domain.models.ClassDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentClassesViewModel @Inject constructor(
    private val getAllClassUseCase: GetAllClassUseCase,
    private val deleteClassUseCase: DeleteClassUseCase,
    private var addNewClassUseCase: AddNewClassUseCase,
    private var dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<ClassDomain, SchoolClassModel>,
    private val classMapper: Mapper<ClassDomain, SchoolClass>,
    private val communication: ItemUiCommunication,
    private val classCommunication: ClassesCommunication,
) : BaseViewModel() {

    fun classAdapterModelsCollect(
        owner: LifecycleOwner,
        observer: Observer<List<ItemUi>>,
    ) = communication.observe(owner = owner, observer = observer)

    fun classesCollect(owner: LifecycleOwner, observer: Observer<List<SchoolClass>>) =
        classCommunication.observe(owner = owner, observer = observer)


    fun fetchClasses(schoolId: String) = launchInBackground {
        getAllClassUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            unravelingResource(resource = resource, schoolId = schoolId)
        }
    }

    private fun unravelingResource(
        resource: Resource<List<ClassDomain>>,
        schoolId: String,
    ) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(QuestionLoadingModel))
            Status.SUCCESS -> {
                val classListDomain = resource.data!!
                classCommunication.put(classListDomain.map { classDomain ->
                    classMapper.map(classDomain)
                })
                communication.put(classListDomain.map { classDomain ->
                    mapper.map(classDomain)
                })
            }
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!
            ) { fetchClasses(schoolId = schoolId) }))
        }
    }

    fun deleteClass(id: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            deleteClassUseCase.execute(id = id).collectLatest { resource ->
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


    fun addNewClass(title: String, schoolId: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            addNewClassUseCase.execute(title = title, schoolId = schoolId)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.LOADING -> showProgressDialog()
                        Status.SUCCESS -> {
                            dismissProgressDialog()
                            emit(resource.data!!)
                        }
                        Status.ERROR -> {
                            dismissProgressDialog()
                            error(message = resource.message!!)
                        }
                    }
                }
        }

    fun goClassStudentFragment(schoolClass: SchoolClass) =
        navigate(FragmentAdminMainRootDirections.actionFragmentAdminMainRootToFragmentClassStudents(
            schoolClass))
}