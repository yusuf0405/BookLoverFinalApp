package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.UploadFileType
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

interface FragmentMainScreenRouter {

    fun navigateToSearchFragment(): NavCommand

    fun navigateToSelectFavoriteBookFragment(): NavCommand

    fun navigateToStoriesFragment(position:Int): NavCommand

    fun navigateToAllSavedBooksFragment(): NavCommand

    fun navigateToAllStudentsFragment(): NavCommand

    fun navigateToAllBooksFragment(): NavCommand

    fun navigateToAllTasksFragment(): NavCommand

    fun navigateToAllAudioBooksFragment(): NavCommand

    fun navigateToAllChaptersFragment(): NavCommand

    fun navigateToUserInfoFragment(userId: String): NavCommand

    fun navigateToBookDetailsFragment(bookId: String): NavCommand

    fun navigateToProfileFragment(): NavCommand

    fun navigateToEditBookFragment(bookId: String): NavCommand

    fun navigateToUploadBookFragment(uploadType: UploadFileType): NavCommand

    fun navigateToGenreInfoFragment(genreId: String): NavCommand

    fun navigateToBookChaptersFragment(path: String, savedBook: BookThatRead): NavCommand

    fun navigateToCreateQuestionNavGraph(bookId: String): NavCommand

}