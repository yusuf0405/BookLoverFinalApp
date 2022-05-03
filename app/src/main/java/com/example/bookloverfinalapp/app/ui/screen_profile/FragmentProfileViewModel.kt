package com.example.bookloverfinalapp.app.ui.screen_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.data.data.cache.db.BooksThatReadDao
import com.example.domain.domain.interactor.ClearBooksCacheUseCase
import com.example.domain.domain.interactor.ClearBooksThatReadCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentProfileViewModel @Inject constructor(
    private val clearBooksThatReadCacheUseCase: ClearBooksThatReadCacheUseCase,
    private val clearBooksCacheUseCase: ClearBooksCacheUseCase,
    private val booksThatReadDao: BooksThatReadDao,
) : BaseViewModel() {

    private val _boosId = MutableLiveData<List<String>>()
    val boosId: LiveData<List<String>> get() = _boosId

    fun clearDataInPhone() = dispatchers.launchInBackground(viewModelScope) {
        val list = mutableListOf<String>()
        booksThatReadDao.getAllBooks().forEach {
            list.add(it.book)
        }
        _boosId.postValue(list)

    }

    fun clearDataInCache() = dispatchers.launchInBackground(viewModelScope) {
        clearBooksCacheUseCase.execute()
        clearBooksThatReadCacheUseCase.execute()
    }

    fun goEditProfileFragment() =
        navigate(FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile())
}