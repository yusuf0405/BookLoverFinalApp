package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookPdfAdapter
import com.example.bookloverfinalapp.app.models.BookPosterAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.adaper.AdminBookAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.adaper.AdminBookItemOnClickListener
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.DialogUpdateBookBinding
import com.example.bookloverfinalapp.databinding.FragmentBooksBinding
import com.example.domain.models.BookPosterDomain
import com.example.domain.models.UpdateBookDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseFile
import com.parse.SaveCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentBooks :
    BaseFragment<FragmentBooksBinding, FragmentBooksViewModel>(FragmentBooksBinding::inflate),
    AdminBookItemOnClickListener {
    override val viewModel: FragmentBooksViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val adapter: AdminBookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AdminBookAdapter(actionListener = this)
    }

    private var dialogBinding: DialogUpdateBookBinding? = null


    private var bookPoster: BookPosterDomain? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().adminBooksRecyclerView.adapter = adapter

        viewModel.fetchBooks(schoolId = currentUser.schoolId)

        viewModel.observe(viewLifecycleOwner) { books -> adapter.books = books.toMutableList() }

        viewModel.bookObserve(viewLifecycleOwner) { books ->
            books.forEach { book ->
                val path = requireActivity().getShPrString(book.objectId)
                if (path == null)
                    viewModel.getBookPdf(book.book.url).observe(viewLifecycleOwner) { inputStream ->
                        createNewPath(inputStream = inputStream, key = book.objectId)
                    }
            }
        }
    }

    override fun tryAgain() {
        viewModel.fetchBooks(schoolId = currentUser.schoolId)
    }

    override fun goChapterBookFragment(book: Book) {
        val path = requireActivity().getShPrString(book.objectId)
        if (path == null) showToast(R.string.book_is_not_ready)
        else viewModel.goChapterFragment(book, path)
    }

    override fun deleteBook(id: String, position: Int) {
        viewModel.deleteBook(id = id)
            .observe(viewLifecycleOwner) { adapter.deleteBook(position = position) }
    }

    override fun updateBook(position: Int, book: Book) {
        dialogBinding = DialogUpdateBookBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding!!.root)
            .setPositiveButton(R.string.action_update, null)
            .setOnCancelListener { dialogBinding = null }
            .create()

        dialog.setOnShowListener {
            dialogBinding?.apply {
                editTextBookTitle.setText(book.title)
                editTextBookAutor.setText(book.author)
                editTextBookPublicYear.setText(book.publicYear)
                requireContext().glide(book.poster.url, roundedBookImage)
            }
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                dialogBinding?.apply {
                    if (editTextBookAutor.text.toString().trim() == book.author &&
                        editTextBookPublicYear.text.toString().trim() == book.publicYear &&
                        editTextBookTitle.text.toString().trim() == book.title
                    ) showToast(R.string.enter_the_change)
                    else {
                        updatedBook(book = book, position = position)
                        dialog.dismiss()
                    }

                }

            }
        }
        dialogBinding?.toChange?.setOnClickListener { getImage() }
        dialog.show()
    }

    private fun updatedBook(book: Book, position: Int) {
        dialogBinding?.apply {
            val title = editTextBookTitle.text.toString().trim()
            val author = editTextBookAutor.text.toString().trim()
            val publicYear = editTextBookPublicYear.text.toString().trim()
            val poster: BookPosterDomain =
                if (bookPoster == null) BookPosterDomain(url = book.poster.url,
                    name = book.poster.name)
                else bookPoster!!
            val updateBook = UpdateBookDomain(
                title = title,
                author = author,
                poster = poster,
                publicYear = publicYear)

            viewModel.updateBook(id = book.objectId, book = updateBook)
                .observe(viewLifecycleOwner) {
                    val newBook = BookAdapterModel.Base(
                        author = author,
                        poster = BookPosterAdapter(url = poster.url, name = poster.name),
                        publicYear = publicYear,
                        page = book.page,
                        chapterCount = book.chapterCount,
                        updatedAt = book.updatedAt,
                        createdAt = book.createdAt,
                        id = book.objectId,
                        book = BookPdfAdapter(url = book.book.url,
                            name = book.book.name,
                            type = "File"),
                        title = title
                    )
                    showToast(R.string.book_updated_successfully)
                    adapter.updateBook(position = position, newBook = newBook)
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data!!
            val poster = ParseFile("image.png", uriToImage(uri))
            requireContext().glide(uri, dialogBinding!!.roundedBookImage)
            poster.saveInBackground(SaveCallback {
                if (it == null) bookPoster = poster.toBookDomainImage()
            })

        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}