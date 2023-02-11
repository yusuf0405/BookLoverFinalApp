package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_time

import android.util.Log
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.listener.ChooseModelOnClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models.ChooseAdapterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentChooseTimeViewModel @Inject constructor(

) : BaseViewModel(), ChooseModelOnClickListeners {

    private val internalItems = mutableListOf<ChooseAdapterModel>()

    private val _itemsFlow = createMutableSharedFlowAsLiveData<List<ChooseAdapterModel>>()
    val itemsFlow get():SharedFlow<List<ChooseAdapterModel>> = _itemsFlow.asSharedFlow()

    init {
        val newItems = generateQuestionsPoints()
        internalItems.clear()
        internalItems.addAll(newItems)
        _itemsFlow.tryEmit(newItems)
    }

    private fun generateQuestionsPoints(): List<ChooseAdapterModel> {
        val items = mutableListOf<ChooseAdapterModel>()
        items.add(createItems(value = 5))
        items.add(createItems(value = 10))
        items.add(createItems(value = 20))
        items.add(createItems(value = 30))
        items.add(createItems(value = 45))
        items.add(createItems(value = 60))
        items.add(createItems(value = 90))
        items.add(createItems(value = 120))
        items.first().isChecked = true
        return items
    }

    private fun createItems(value: Int) = ChooseAdapterModel(
        title = "$value sec", value = value, listener = this, isChecked = false
    )

    fun update(newItems: List<ChooseAdapterModel>) {
        internalItems.clear()
        internalItems.addAll(newItems)
    }

    override fun chooseModelOnClickListener(item: ChooseAdapterModel) {
        val checkedItemIndex = internalItems.indexOf(item)
        val newItems = internalItems.onEach { time -> time.isChecked = false }
        newItems[checkedItemIndex].isChecked = true
        _itemsFlow.tryEmit(newItems)
    }

}