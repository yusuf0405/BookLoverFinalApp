package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.base.GenericAdapter
import com.example.bookloverfinalapp.app.base.ItemOnClickListener
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_ADMIN_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.DialogUpdateBookBinding
import com.example.bookloverfinalapp.databinding.FragmentAdminBooksBinding
import com.example.domain.models.BookPosterDomain
import com.example.domain.models.UpdateBookDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseFile
import com.parse.SaveCallback
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class FragmentBooks :
    BaseFragment<FragmentAdminBooksBinding, FragmentBooksViewModel>(FragmentAdminBooksBinding::inflate),
    ItemOnClickListener, SearchView.OnQueryTextListener {

    override val viewModel: FragmentBooksViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    private var dialogBinding: DialogUpdateBookBinding? = null

    private var bookPoster: BookPosterDomain? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().adminBooksRecyclerView.adapter = adapter

        binding().searchEditText.setOnQueryTextListener(this)

        viewModel.fetchBooks(schoolId = currentUser.schoolId)

        viewModel.booksAdapterModelCollect(viewLifecycleOwner) { books ->
            adapter.map(books.swapElements().toMutableList())
        }

        viewModel.booksCollect(viewLifecycleOwner) { books ->
            books.forEach { book ->
                val path = requireActivity().getShPrString(book.objectId)
                if (path == null)
                    viewModel.getBookPdf(book.book.url).observe(viewLifecycleOwner) { inputStream ->
                        createNewPath(inputStream = inputStream, key = book.objectId)
                    }
            }
        }
    }

    override fun updateItem(item: ItemUi, position: Int) {
        val bookUi = item as BookModel
        val book = viewModel.adapterMapper.map(bookUi)
        dialogBinding = DialogUpdateBookBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding!!.root)
            .setPositiveButton(R.string.action_update, null)
            .setNeutralButton(R.string.action_ignore, null)
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
                    val newBook = BookModel(
                        author = author,
                        poster = BookModel.BookPosterAdapter(url = poster.url, name = poster.name),
                        publicYear = publicYear,
                        page = book.page,
                        chapterCount = book.chapterCount,
                        updatedAt = book.updatedAt,
                        createdAt = book.createdAt,
                        id = book.objectId,
                        book = BookModel.BookPdfAdapter(url = book.book.url,
                            name = book.book.name,
                            type = "File"),
                        title = title,
                        genres = book.genres,
                        viewHolderType = ADAPTER_ADMIN_BOOK_VIEW_HOLDER
                    )
                    showToast(R.string.book_updated_successfully)
                    adapter.updateItem(position = position, newItem = newBook)
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
                loadingDialog.show()
                if (it == null) {
                    loadingDialog.dismiss()
                    bookPoster = poster.toBookDomainImage()
                } else loadingDialog.dismiss()
            })
        }
    }

    override fun onQueryTextSubmit(searchText: String?): Boolean {
        if (searchText != null) viewModel.searchBook(searchText, schoolId = currentUser.schoolId)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            val searchText = newText.lowercase(Locale.getDefault())
            viewModel.searchBook(searchText, schoolId = currentUser.schoolId)
        } else viewModel.fetchBooks(schoolId = currentUser.schoolId)
        return false
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }

    override fun showAnotherFragment(item: ItemUi) {
        val bookUi = item as BookModel
        val book = viewModel.adapterMapper.map(bookUi)
        val path = requireActivity().getShPrString(book.objectId)
        if (path == null) showToast(R.string.book_is_not_ready)
        else viewModel.goChapterFragment(book, path)
    }

    override fun deleteItem(item: ItemUi, position: Int) {
        val bookUi = item as BookModel
        val id = viewModel.adapterMapper.map(bookUi).objectId
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                    viewModel.deleteBook(id = id)
                        .observe(viewLifecycleOwner) { adapter.deleteItem(position = position) }

                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setCancelable(true)
            .setMessage(R.string.default_book_delete_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()

        dialog.show()
    }

}