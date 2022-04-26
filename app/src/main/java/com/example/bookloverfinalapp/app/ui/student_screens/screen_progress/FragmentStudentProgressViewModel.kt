package com.example.bookloverfinalapp.app.ui.student_screens.screen_progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.utils.communication.StudentBooksCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.GetStudentBookUseCase
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentStudentProgressViewModel @Inject constructor(
    private val communication: StudentBooksCommunication,
    private val studentBookUseCase: GetStudentBookUseCase,
    private val mapper: Mapper<StudentBookDomain, StudentBook>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<StudentBook>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        studentBookUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressAnimation()
                Status.SUCCESS -> {
                    communication.map(resource.data!!.map { studentBookDomain ->
                        mapper.map(studentBookDomain)
                    })
                    dismissProgressAnimation()
                }
                else -> {
                    error(message = resource.message!!)
                    dismissProgressAnimation()
                }
            }
        }
    }

    fun go() =
        navigate(FragmentStudentProgressDirections.actionFragmentStudentProgressToFragmentAdminUploadPdf())
}