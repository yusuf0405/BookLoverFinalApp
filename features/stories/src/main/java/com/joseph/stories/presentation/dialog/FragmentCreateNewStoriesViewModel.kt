package com.joseph.stories.presentation.dialog

import androidx.lifecycle.viewModelScope
import com.joseph.common_api.DispatchersProvider
import com.joseph.common_api.base.BaseViewModel
import com.joseph.stories.domain.repositories.StoriesFeatureRepository
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ProgressCallback
import com.parse.SaveCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentCreateNewStoriesViewModel @Inject constructor(
    private val storiesFeatureRepository: StoriesFeatureRepository,
    private val dispatchersProvider: DispatchersProvider,
) : BaseViewModel(), SaveCallback, ProgressCallback {

    private val _startAddNewStories = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val startAddNewStories get() = _startAddNewStories.asSharedFlow()

    private val _imageUploadDialogIsShowFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val imageUploadDialogIsShowFlow get() = _imageUploadDialogIsShowFlow.asSharedFlow()

    private val _isShowProgressDialogFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isShowProgressDialogFlow get() = _isShowProgressDialogFlow.asSharedFlow()

    private val _imageUploadDialogPercentFlow = createMutableSharedFlowAsSingleLiveEvent<Int>()
    val imageUploadDialogPercentFlow get() = _imageUploadDialogPercentFlow.asSharedFlow()

    private val _storiesAddSuccessfully = createMutableSharedFlowAsSingleLiveEvent<Unit>()
    val storiesAddSuccessfully get() = _storiesAddSuccessfully.asSharedFlow()

    private var parseFile: ParseFile? = null

    fun startSaveFile(byteArray: ByteArray?) = viewModelScope.launch {
        parseFile = ParseFile(byteArray)
        _imageUploadDialogIsShowFlow.tryEmit(true)
        parseFile?.saveInBackground(
            this@FragmentCreateNewStoriesViewModel,
            this@FragmentCreateNewStoriesViewModel
        )
    }


    fun addNewStories(
        title: String,
        description: String,
        isVideoFile: Boolean,
    ) {
        _imageUploadDialogIsShowFlow.tryEmit(false)
        _isShowProgressDialogFlow.tryEmit(true)
//        val currentUser = userCacheRepository.fetchCurrentUserFromCache()
        var imageFileUrl = String()
        var videoFileUrl = String()
        if (isVideoFile) videoFileUrl = parseFile?.url ?: String()
        else imageFileUrl = parseFile?.url ?: String()
//        val addStoriesModel = AddStoriesDomain(
//            title = title,
//            description = description,
//            userId = currentUser.id,
//            schoolId = currentUser.schoolId,
//            isVideoFile = isVideoFile,
//            previewImageUrl = currentUser.image.url,
//            imageFileUrl = imageFileUrl,
//            videoFileUrl = videoFileUrl
//        )
//        viewModelScope.launchSafe(
//            safeAction = { storiesRepository.addNewStories(addStoriesModel) },
//            dispatcher = dispatchersProvider.io(),
//            onSuccess = {
//                _storiesAddSuccessfully.tryEmit(Unit)
//                _isShowProgressDialogFlow.tryEmit(false)
//            },
//            onError = {
//                _isShowProgressDialogFlow.tryEmit(false)
//            }
//        )

    }

    override fun done(e: ParseException?) {
        if (e != null) _imageUploadDialogIsShowFlow.tryEmit(false)
        _startAddNewStories.tryEmit(Unit)
    }

    override fun done(percentDone: Int) {
        _imageUploadDialogPercentFlow.tryEmit(percentDone)
    }
}