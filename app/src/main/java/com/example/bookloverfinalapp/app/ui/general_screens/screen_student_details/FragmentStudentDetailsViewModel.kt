package com.example.bookloverfinalapp.app.ui.general_screens.screen_student_details

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.*
import com.example.bookloverfinalapp.app.ui.adapter.*
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.DeleteUserUseCase
import com.example.domain.interactor.GetMyStudentBooksUseCase
import com.example.domain.models.BookThatReadDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentStudentDetailsViewModel @Inject constructor(
    private val myStudentBooksUseCase: GetMyStudentBooksUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val communication: ItemUiCommunication,
    private val mapper: Mapper<BookThatReadDomain, BookThatReadModel>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = launchInBackground {
        myStudentBooksUseCase.execute(id).collectLatest { resource ->
            unravelingSchoolsStudents(resource = resource, id = id)
        }
    }

    private fun unravelingSchoolsStudents(
        resource: Resource<List<BookThatReadDomain>>,
        id: String,
    ) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(BookThatReadLoadingModel))
            Status.SUCCESS -> communication.put(resource.data!!.map { studentBookDomain ->
                mapper.map(studentBookDomain)
            })
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!) { fetchMyBook(id = id) }))

        }
    }

    fun deleteStudent(id: String, sessionToken: String) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            deleteUserUseCase.execute(id, sessionToken).collectLatest { resource ->
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

    fun goBack() = navigateBack()
}