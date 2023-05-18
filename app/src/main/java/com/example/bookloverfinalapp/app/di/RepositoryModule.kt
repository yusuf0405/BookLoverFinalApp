package com.example.bookloverfinalapp.app.di

import android.content.Context
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.data.ResourceProvider
import com.example.data.cache.models.ClassCache
import com.example.data.cache.models.UserCache
import com.example.data.cache.source.classes.ClassCacheDataSource
import com.example.data.cache.source.users.UsersCacheDataSource
import com.example.data.cloud.models.SignUpAnswerCloud
import com.example.data.cloud.models.UserCloud
import com.example.data.cloud.service.LoginService
import com.example.data.cloud.service.SchoolService
import com.example.data.cloud.source.saved_books.BooksThatReadCloudDataSource
import com.example.data.cloud.source.school_classes.ClassCloudDataSource
import com.example.data.cloud.users.UsersCloudDataSource
import com.example.data.models.*
import com.example.data.repository.*
import com.example.bookloverfinalapp.app.ui.service_uplaod.book_save_to_file.BooksSaveToFileRepositoryImpl
import com.example.data.repository.login.LoginRepositoryImpl
import com.example.data.repository.school.SchoolRepositoryImpl
import com.example.data.repository.school_classes.ClassRepositoryImpl
import com.example.data.repository.user_cache.UserCacheRepositoryImpl
import com.example.data.repository.users.UserRepositoryImpl
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.*
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        service: LoginService,
        signInMapper: Mapper<UserCloud, UserDomain>,
        signUpMapper: Mapper<SignUpAnswerCloud, PostRequestAnswerDomain>,
        resourceProvider: ResourceProvider,
    ): LoginRepository =
        LoginRepositoryImpl(
            service = service,
            signInMapper = signInMapper,
            signUpMapper = signUpMapper,
            resourceProvider = resourceProvider
        )

    @Provides
    @Singleton
    fun provideUserCacheRepository(
        @ApplicationContext context: Context,
        mapperToDomain: Mapper<UserSaveModel, UserDomain>,
        mapperToSaveModel: Mapper<UserDomain, UserSaveModel>
    ): UserCacheRepository =
        UserCacheRepositoryImpl(
            context = context,
            mapperToDomain = mapperToDomain,
            mapperToSaveModel = mapperToSaveModel
        )

    @Provides
    @Singleton
    fun provideSchoolRepository(
        service: SchoolService,
        resourceProvider: ResourceProvider,
    ): SchoolRepository =
        SchoolRepositoryImpl(service = service, resourceProvider = resourceProvider)


    @Provides
    @Singleton
    fun provideUserRepository(
        cloudDataSource: UsersCloudDataSource,
        cacheDataSource: UsersCacheDataSource,
        savedBookCloudDataSource: BooksThatReadCloudDataSource,
        dispatchersProvider: DispatchersProvider,
        studentDomainMapper: Mapper<StudentData, StudentDomain>,
        userCloudMapper: Mapper<UserCloud, UserDomain>,
        userCacheMapper: Mapper<UserCache, StudentData>,
    ): UserRepository =
        UserRepositoryImpl(
            cloudDataSource = cloudDataSource,
            cacheDataSource = cacheDataSource,
            studentDomainMapper = studentDomainMapper,
            dispatchersProvider = dispatchersProvider,
            userCloudToDomainMapper = userCloudMapper,
            userCacheMapper = userCacheMapper,
            savedBookCloudDataSource = savedBookCloudDataSource,
        )

    @Provides
    @Singleton
    fun provideClassRepository(
        cloudDataSource: ClassCloudDataSource,
        cacheDataSource: ClassCacheDataSource,
        classMapper: Mapper<ClassData, ClassDomain>,
        classCashMapper: Mapper<ClassCache, ClassData>,
    ): ClassRepository = ClassRepositoryImpl(
        cloudDataSource = cloudDataSource,
        cacheDataSource = cacheDataSource,
        classMapper = classMapper,
        classCashMapper = classCashMapper,
    )

    @Provides
    @Singleton
    fun provideBooksSaveToFileRepository(
        @ApplicationContext context: Context,
        dispatchersProvider: DispatchersProvider,
    ): BooksSaveToFileRepository = BooksSaveToFileRepositoryImpl(
        context = context,
        dispatchersProvider = dispatchersProvider
    )
}