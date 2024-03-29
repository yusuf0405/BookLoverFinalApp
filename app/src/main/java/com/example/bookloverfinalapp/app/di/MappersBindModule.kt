package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.mappers.AudioBookDomainToUiMapper
import com.example.bookloverfinalapp.app.mappers.GenreDomainToUiMapper
import com.example.bookloverfinalapp.app.mappers.StoriesDomainToUiMapper
import com.example.bookloverfinalapp.app.mappers.TaskDomainToAdapterModelMapper
import com.example.bookloverfinalapp.app.models.AudioBook
import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.mappers.UserDomainToAdapterModelMapperImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.mappers.GenreItemsToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.mappers.GenreItemsToAdapterModelMapperImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.mappers.StudentDomainToUserRatingModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.mappers.StudentDomainToUserRatingModelMapperImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.TaskAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.mappers.ItemsToSearchFilteredModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.mappers.ItemsToSearchFilteredModelMapperImpl
import com.example.data.cache.mappers.AudioBookCacheToDataMapper
import com.example.data.cache.mappers.GenreCacheToDataMapper
import com.example.data.cache.mappers.StoriesCacheToDataMapper
import com.example.data.cache.mappers.TaskCacheToDataMapper
import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.GenreCache
import com.example.data.cache.models.StoriesCache
import com.example.data.cache.models.TaskCache
import com.example.data.cloud.mappers.*
import com.example.data.cloud.models.*
import com.example.data.mappers.*
import com.example.data.models.*
import com.example.domain.Mapper
import com.example.domain.models.*
import com.joseph.stories.presentation.mappers.StoriesToUserStoriesMapper
import com.joseph.stories.presentation.mappers.StoriesToUserStoriesMapperImpl
import com.joseph.stories.presentation.models.StoriesModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MappersBindModule {


    @Binds
    abstract fun bindUserDomainToAdapterModelMapper(
        impl: UserDomainToAdapterModelMapperImpl
    ): UserDomainToAdapterModelMapper

    @Binds
    abstract fun bindStudentDomainToUserRatingModelMapper(
        impl: StudentDomainToUserRatingModelMapperImpl
    ): StudentDomainToUserRatingModelMapper

    @Binds
    abstract fun bindGenreItemsToAdapterModelMapper(
        impl: GenreItemsToAdapterModelMapperImpl
    ): GenreItemsToAdapterModelMapper

    @Binds
    abstract fun bindAudioBookCloudToDataMapper(
        impl: AudioBookCloudToDataMapper
    ): Mapper<AudioBookCloud, AudioBookData>

    @Binds
    abstract fun bindAudioBookCacheToDataMapper(
        impl: AudioBookCacheToDataMapper
    ): Mapper<AudioBookCache, AudioBookData>

    @Binds
    abstract fun bindAudioBookDataToCacheMapper(
        impl: AudioBookDataToCacheMapper
    ): Mapper<AudioBookData, AudioBookCache>

    @Binds
    abstract fun bindAudioBookDataToDomainMapper(
        impl: AudioBookDataToDomainMapper
    ): Mapper<AudioBookData, AudioBookDomain>


    @Binds
    abstract fun bindAudioBookDomainToUiMapper(
        impl: AudioBookDomainToUiMapper
    ): Mapper<AudioBookDomain, AudioBook>

    @Binds
    abstract fun bindTaskCacheToDataMapper(
        impl: TaskCacheToDataMapper
    ): Mapper<TaskCache, TaskData>

    @Binds
    abstract fun bindTaskCloudToDataMapper(
        impl: TaskCloudToDataMapper
    ): Mapper<TaskCloud, TaskData>

    @Binds
    abstract fun bindTaskDataToDomainMapper(
        impl: TaskDataToDomainMapper
    ): Mapper<TaskData, TaskDomain>

    @Binds
    abstract fun bindTaskDataToCacheMapper(
        impl: TaskDataToCacheMapper
    ): Mapper<TaskData, TaskCache>

    @Binds
    abstract fun bindTaskDomainToAdapterModelMapper(
        impl: TaskDomainToAdapterModelMapper
    ): Mapper<TaskDomain, TaskAdapterModel>


    @Binds
    abstract fun bindBookResponseToBookCloudMapper(
        impl: BookResponseToBookCloudMapper
    ): Mapper<BookResponse, BookCloud>


    @Binds
    abstract fun bindGenreCloudToDataMapper(
        impl: GenreCloudToDataMapper
    ): Mapper<GenreCloud, GenreData>

    @Binds
    abstract fun bindGenreCacheToDataMapper(
        impl: GenreCacheToDataMapper
    ): Mapper<GenreCache, GenreData>

    @Binds
    abstract fun bindGenreDataToDomainMapper(
        impl: GenreDataToDomainMapper
    ): Mapper<GenreData, GenreDomain>

    @Binds
    abstract fun bindGenreDataToCacheMapper(
        impl: GenreDataToCacheMapper
    ): Mapper<GenreData, GenreCache>


    @Binds
    abstract fun bindItemsToSearchFilteredModelMapper(
        impl: ItemsToSearchFilteredModelMapperImpl
    ): ItemsToSearchFilteredModelMapper

    @Binds
    abstract fun bindMainItemsToSearchFilteredModelMapper(
        impl: MainItemsToSearchFilteredModelMapperImpl
    ): MainItemsToSearchFilteredModelMapper

    @Binds
    abstract fun bindGenreDomainToUiMapper(
        impl: GenreDomainToUiMapper
    ): Mapper<GenreDomain, Genre>


    @Binds
    abstract fun bindStoriesCacheToDataMapper(
        impl: StoriesCacheToDataMapper
    ): Mapper<StoriesCache, StoriesData>

    @Binds
    abstract fun bindStoriesDataToCacheMapper(
        impl: StoriesDataToCacheMapper
    ): Mapper<StoriesData, StoriesCache>


    @Binds
    abstract fun bindStoriesCloudToDataMapper(
        impl: StoriesCloudToDataMapper
    ): Mapper<StoriesCloud, StoriesData>

    @Binds
    abstract fun bindStoriesDataToDomainMapper(
        impl: StoriesDataToDomainMapper
    ): Mapper<StoriesData, StoriesDomain>

    @Binds
    abstract fun bindAddStoriesDomainToDataMapper(
        impl: AddStoriesDomainToDataMapper
    ): Mapper<AddStoriesDomain, AddStoriesData>


    @Binds
    abstract fun bindAddStoriesDataToCloudMapper(
        impl: AddStoriesDataToCloudMapper
    ): Mapper<AddStoriesData, AddStoriesCloud>


    @Binds
    abstract fun bindStoriesDomainToUiMapper(
        impl: StoriesDomainToUiMapper
    ): Mapper<StoriesDomain, StoriesModel>

    @Binds
    abstract fun bindStoriesToUserStoriesMapper(
        impl: StoriesToUserStoriesMapperImpl
    ): StoriesToUserStoriesMapper

    @Binds
    abstract fun bindBookToExclusiveBookMapper(
        impl: BookToExclusiveBookMapperImpl
    ): BookToExclusiveBookMapper

    @Binds
    abstract fun bindBookToHorizontalBookMapper(
        impl: BookToHorizontalBookMapperImpl
    ): BookToHorizontalBookMapper

    @Binds
    abstract fun bindGenreMapBookGenreItemMapper(
        impl: GenreMapBookGenreItemMapperImpl
    ): GenreMapBookGenreItemMapper

    @Binds
    abstract fun bindUserStoriesToStoriesItemMapper(
        impl: UserStoriesToStoriesItemMapperImpl
    ): UserStoriesToStoriesItemMapper

    @Binds
    abstract fun bindAudioBookToExclusiveAudioBookMapper(
        impl: AudioBookToExclusiveAudioBookMapperImpl
    ): AudioBookToExclusiveAudioBookMapper
}
