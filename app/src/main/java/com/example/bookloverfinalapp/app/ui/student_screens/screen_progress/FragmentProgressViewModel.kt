package com.example.bookloverfinalapp.app.ui.student_screens.screen_progress

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.GetBookThatReadUseCase
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentProgressViewModel @Inject constructor(
    private val communication: BooksThatReadCommunication,
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val mapper: Mapper<BookThatReadDomain, BookThatRead>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        bookThatReadUseCase.execute(id).collectLatest { resource ->
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
        navigate(FragmentProgressDirections.actionFragmentProgressToFragmentAdminUploadPdf())
}