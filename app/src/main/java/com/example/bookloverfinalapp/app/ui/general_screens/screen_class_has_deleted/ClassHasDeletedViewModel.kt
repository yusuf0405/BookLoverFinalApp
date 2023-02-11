package com.example.bookloverfinalapp.app.ui.general_screens.screen_class_has_deleted

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.utils.communication.ClassesCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.ClassDomain
import com.example.domain.repository.ClassRepository
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ClassHasDeletedViewModel @Inject constructor(
    private var classRepository: ClassRepository,
    private val userRepository: UserRepository,
    private val dispatchersProvider: DispatchersProvider,
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
    ) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = {
                userRepository.updateStudentClass(
                    id = id,
                    sessionToken = sessionToken,
                    classId = classId,
                    classTitle = classTitle
                )
            },
            onSuccess = {

            },
            onError = {

            }
        )
    }

    fun fetchClasses(schoolId: String) = launchInBackground {
        classRepository.fetchAllClass(schoolId = schoolId).collectLatest { resource ->
//            when (resource.status) {
//                Status.LOADING -> showProgressDialog()
//                Status.SUCCESS -> {
//                    dismissProgressDialog()
//                    communication.put(resource.data!!.map { classDomain ->
//                        classMapper.map(classDomain)
//                    })
//                }
//                Status.EMPTY -> {}
//                Status.ERROR -> {
//                    dismissProgressDialog()
//                    error(message = resource.message!!)
//                }
//            }
        }
    }
}
