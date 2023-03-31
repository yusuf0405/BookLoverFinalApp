package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.choose_online

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.domain.DispatchersProvider
import com.example.domain.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentChoosePosterOnlineViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository,
    private val dispatchersProvider: DispatchersProvider
) : BaseViewModel() {

    private val _postersUrlFlow = createMutableSharedFlowAsLiveData<List<String>>()
    val postersUrlFlow: SharedFlow<List<String>> = _postersUrlFlow.asSharedFlow()

    init {
        viewModelScope.launch(dispatchersProvider.io()) {
            val posters = questionsRepository.fetchAllQuestionsPosters()
            _postersUrlFlow.tryEmit(posters)
        }
    }
}