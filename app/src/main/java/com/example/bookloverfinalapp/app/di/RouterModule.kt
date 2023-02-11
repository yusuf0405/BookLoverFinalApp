package com.example.bookloverfinalapp.app.di

import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router.FragmentChaptersForCreateQuestionRouter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.router.FragmentChaptersForCreateQuestionRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.router.FragmentAllBooksRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.router.FragmentAllBooksRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.router.FragmentAllGenresRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_genres.router.FragmentAllGenresRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.router.FragmentAllSavedBooksRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.router.FragmentAllSavedBooksRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router.FragmentBookInfoRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.router.FragmentBookInfoRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router.FragmentChapterBookRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.router.FragmentChapterBookRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router.FragmentGenreInfoRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router.FragmentGenreInfoRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.router.FragmentProgressRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.router.FragmentProgressRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.router.FragmentMainScreenRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.router.FragmentMainScreenRouterImpl
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.router.FragmentSearchRouter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_search.router.FragmentSearchRouterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RouterModule {

    @Binds
    abstract fun provideFragmentMainScreenRouter(router: FragmentMainScreenRouterImpl): FragmentMainScreenRouter

    @Binds
    abstract fun provideFragmentAllBooksRouter(router: FragmentAllBooksRouterImpl): FragmentAllBooksRouter

    @Binds
    abstract fun provideFragmentAllSavedBooksRouter(router: FragmentAllSavedBooksRouterImpl): FragmentAllSavedBooksRouter

    @Binds
    abstract fun provideFragmentChapterBookRouter(router: FragmentChapterBookRouterImpl): FragmentChapterBookRouter

    @Binds
    abstract fun provideFragmentAllGenresRouter(router: FragmentAllGenresRouterImpl): FragmentAllGenresRouter

    @Binds
    abstract fun bindFragmentProgressRouter(
        impl: FragmentProgressRouterImpl
    ): FragmentProgressRouter

    @Binds
    abstract fun bindFragmentChaptersForCreateQuestionRouter(
        impl: FragmentChaptersForCreateQuestionRouterImpl
    ): FragmentChaptersForCreateQuestionRouter


    @Binds
    abstract fun bindFragmentBookInfoRouter(
        impl: FragmentBookInfoRouterImpl
    ): FragmentBookInfoRouter

    @Binds
    abstract fun bindFragmentGenreInfoRouter(
        impl: FragmentGenreInfoRouterImpl
    ): FragmentGenreInfoRouter

    @Binds
    abstract fun bindFragmentSearchRouter(
        impl: FragmentSearchRouterImpl
    ): FragmentSearchRouter


}