package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.mappers.StudentDomainToUserRatingModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.FetchRatingType
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentLeaderboardChartViewModel @Inject constructor(
    userCacheRepository: UserCacheRepository,
    private val userRepository: UserRepository,
    private val studentDomainToUserRatingModelMapper: StudentDomainToUserRatingModelMapper,
) : BaseViewModel() {

    private val currentUserFlow = userCacheRepository.fetchCurrentUserFromCacheFlow()
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val fetchRatingType = MutableStateFlow(FetchRatingType.CLASS)

    val studentsFlow = fetchRatingType.flatMapLatest(::checkFetchType)
        .map(studentDomainToUserRatingModelMapper::map)
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val studentsFromClass = currentUserFlow.flatMapLatest {
        userRepository.fetchAllStudentsFromClassId(
            classId = it.classId,
            schoolId = it.schoolId,
            currentUserId = String()
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateFetchRatingType(type: FetchRatingType) = fetchRatingType.tryEmit(type)

    private val studentsFromSchool = currentUserFlow.map { it.schoolId }
        .flatMapLatest(userRepository::fetchSchoolStudents)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun checkFetchType(type: FetchRatingType) = when (type) {
        FetchRatingType.CLASS -> studentsFromClass
        FetchRatingType.SCHOOL -> studentsFromSchool
    }

    data class ClassStatistics(
        var readPages: Int,
        var readChapters: Int,
        var readBooks: Int,
    )
}