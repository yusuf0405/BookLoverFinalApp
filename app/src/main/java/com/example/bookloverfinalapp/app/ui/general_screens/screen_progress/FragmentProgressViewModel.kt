package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import androidx.lifecycle.viewModelScope
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.router.FragmentProgressRouter
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.HeaderItem
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.mappers.StudentDomainToUserRatingModelMapper
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.StudentDomain
import com.example.domain.models.UserDomain
import com.example.domain.models.UserStatisticModel
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import com.example.domain.repository.UserStatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentProgressViewModel @Inject constructor(
    private val userStatisticsRepository: UserStatisticsRepository,
    userCacheRepository: UserCacheRepository,
    private val userRepository: UserRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val router: FragmentProgressRouter,
    private val mapUserDomainToUiMapper: Mapper<UserDomain, User>,
    private val studentDomainToUserRatingModelMapper: StudentDomainToUserRatingModelMapper,
    private val studentDomainToUiMapper: Mapper<StudentDomain, Student>,
) : BaseViewModel() {

    private val _userStatisticDays = MutableStateFlow<List<UserStatisticModel>>(emptyList())
    val userStatisticDays: StateFlow<List<UserStatisticModel>> get() = _userStatisticDays.asStateFlow()

    private val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .map(mapUserDomainToUiMapper::map)
        .flowOn(dispatchersProvider.io())

    val userAndProgressFlow = currentUserFlow.map { it.id }
//        .map(userRepository::fetchUserFromId)
//        .map(studentDomainToUiMapper::map)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val studentsFromClass = currentUserFlow.flatMapLatest {
        userRepository.fetchAllStudentsFromClassId(
            classId = it.classId,
            schoolId = it.schoolId,
            currentUserId = String()
        )
    }.map { it.sortedByDescending { it.progress } }
        .map {
            val topRatedUsers = mutableListOf<StudentDomain>()
            if (it.isNotEmpty()) topRatedUsers.add(it.first())
            if (it.size > 1) topRatedUsers.add(it[1])
            if (it.size > 2) topRatedUsers.add(it[2])
            topRatedUsers
        }
        .map(studentDomainToUserRatingModelMapper::map)
        .map(::addHeaderToRecyclerView)
        .flowOn(dispatchersProvider.default())
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun fetchUserStatisticDays() {
        viewModelScope.launch(dispatchersProvider.io()) {
            _userStatisticDays.tryEmit(userStatisticsRepository.fetchStatisticDays())
        }
    }

    fun navigateToAllSavedBooksFragment() {
        navigate(router.navigateToAllSavedBooksFragment())
    }

    private fun addHeaderToRecyclerView(items: List<Item>): List<Item> {
        val mutableItems = mutableListOf<Item>()
        if (items.isNotEmpty()) {
            mutableItems.add(
                HeaderItem(
                    titleId = IdResourceString(R.string.leaderboard),
                    onClickListener = { navigateToFragmentLeaderboardChart() }
                )
            )
            mutableItems.add(items.first())
        }
        return mutableItems
    }

    private fun navigateToFragmentLeaderboardChart() {
        navigate(router.navigateToFragmentLeaderboardChart())
    }
}