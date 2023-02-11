package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_class

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.UserDomain
import com.example.domain.repository.ClassRepository
import com.example.domain.repository.LoginRepository
import com.example.domain.repository.UserCacheRepository
import com.example.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val SCHOOL_ID_KEY = "SCHOOL_ID"
private const val USER_SIGN_UP_KEY = "USER_SIGN_UP_KEY"

class FragmentChoiceClassViewModelFactory @AssistedInject constructor(
    @Assisted(SCHOOL_ID_KEY) private val schoolId: String,
    @Assisted(USER_SIGN_UP_KEY) private val userSignUp: UserSignUp,
    private val resourceProvider: ResourceProvider,
    private val mapper: Mapper<User, UserDomain>,
    private val userCacheRepository: UserCacheRepository,
    private val userRepository: UserRepository,
    private var loginRepository: LoginRepository,
    private val classRepository: ClassRepository,
    private val dispatchersProvider: DispatchersProvider,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentChoiceClassViewModel::class.java)
        return FragmentChoiceClassViewModel(
            schoolId = schoolId,
            userSignUp = userSignUp,
            userCacheRepository = userCacheRepository,
            userRepository = userRepository,
            dispatchersProvider = dispatchersProvider,
            repository = classRepository,
            resourceProvider = resourceProvider,
            mapper = mapper,
            loginRepository = loginRepository
        ) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted(SCHOOL_ID_KEY) schoolId: String,
            @Assisted(USER_SIGN_UP_KEY) userSignUp: UserSignUp
        ): FragmentChoiceClassViewModelFactory
    }
}