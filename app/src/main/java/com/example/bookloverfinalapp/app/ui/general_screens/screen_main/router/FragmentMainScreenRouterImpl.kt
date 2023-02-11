package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.router

import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book.UploadFileType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.FragmentMainScreenDirections
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand
import com.example.bookloverfinalapp.app.utils.navigation.toNavCommand
import javax.inject.Inject

class FragmentMainScreenRouterImpl @Inject constructor() : FragmentMainScreenRouter {

    override fun navigateToSearchFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToSearchNavGraph()
            .toNavCommand()

    override fun navigateToAllSavedBooksFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentMyBooks()
            .toNavCommand()

    override fun navigateToAllStudentsFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentMyStudents()
            .toNavCommand()

    override fun navigateToAllBooksFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentAllBooks()
            .toNavCommand()

    override fun navigateToAllTasksFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentAllTasks()
            .toNavCommand()

    override fun navigateToAllAudioBooksFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentAllAudioBooks()
            .toNavCommand()

    override fun navigateToAllChaptersFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentAllGenres()
            .toNavCommand()

    override fun navigateToUserInfoFragment(userId: String): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToUserInfoNavGraph(userId = userId)
            .toNavCommand()

    override fun navigateToBookDetailsFragment(bookId: String): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToBookInfoNavGraph(bookId)
            .toNavCommand()

    override fun navigateToProfileFragment(): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToProfileNavGraph()
            .toNavCommand()

    override fun navigateToEditBookFragment(bookId: String): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentEditBook(bookId = bookId)
            .toNavCommand()

    override fun navigateToUploadBookFragment(uploadType: UploadFileType): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToFragmentAdminUploadPdf(uploadType)
            .toNavCommand()

    override fun navigateToGenreInfoFragment(genreId: String): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToBookGenreInfoNavGraph(genreId)
            .toNavCommand()

    override fun navigateToBookChaptersFragment(path: String, savedBook: BookThatRead) =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToBookReadNavGraph(path = path, book = savedBook)
            .toNavCommand()

    override fun navigateToCreateQuestionNavGraph(bookId: String): NavCommand =
        FragmentMainScreenDirections
            .actionFragmentMainScreenToCreateQuestionNavGraph(bookId = bookId)
            .toNavCommand()

}