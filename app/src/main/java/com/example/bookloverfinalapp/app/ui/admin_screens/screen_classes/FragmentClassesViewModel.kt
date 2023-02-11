package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.CatEmptyModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.SchoolClassModel
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root.FragmentAdminMainRootDirections
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain
import com.example.domain.repository.ClassRepository
import com.joseph.ui_core.custom.ItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FragmentClassesViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private var repository: ClassRepository,
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


    fun fetchClasses(schoolId: String) {
        repository.fetchAllClass(schoolId = schoolId)
            .onEach { if (it.isEmpty()) communication.put(listOf(CatEmptyModel)) }
            .map(::mapToPairElement)
            .onEach(::putResourceToCommunication)
            .launchIn(viewModelScope)
    }

    private fun mapToPairElement(classes: List<ClassDomain>) =
        Pair(classes.map(classMapper::map), classes.map(mapper::map))


    private fun putResourceToCommunication(resource: Pair<List<SchoolClass>, List<SchoolClassModel>>) {
        classCommunication.put(resource.first)
        communication.put(resource.second)
    }

//    private fun unravelingResource(
//        resource: Resource<List<ClassDomain>>,
//        schoolId: String,
//    ) {
//        when (resource.status) {
//            Status.LOADING -> communication.put(listOf(QuestionLoadingModel))
//            Status.SUCCESS -> {
//                val classListDomain = resource.data!!
//                val list = classListDomain.map { classDomain ->
//                    classMapper.map(classDomain)
//                }
//
//                communication.put(classListDomain.map { classDomain ->
//                    mapper.map(classDomain)
//                })
//            }
//            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
//            Status.ERROR -> communication.put(
//                listOf(ErrorModel(
//                    resource.message!!
//                ) { fetchClasses(schoolId = schoolId) })
//            )
//        }
//    }

    fun deleteClass(id: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.deleteClass(id) },
            onSuccess = {

            },
            onError = ::handleError
        )
    }


    fun addNewClass(title: String, schoolId: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.addClass(title = title, schoolId = schoolId) },
            onError = ::handleError,
            onSuccess = {

            }
        )
    }

    private fun handleError(error: Throwable) {
        emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(error))
    }

    fun goClassStudentFragment(schoolClass: SchoolClass) =
        navigate(
            FragmentAdminMainRootDirections.actionFragmentAdminMainRootToFragmentClassStudents(
                schoolClass
            )
        )
}