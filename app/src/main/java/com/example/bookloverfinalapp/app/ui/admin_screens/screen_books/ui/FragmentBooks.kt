package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.AdminBookAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.AdminBookItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.createNewPath
import com.example.bookloverfinalapp.app.utils.extensions.getShPrString
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentBooksBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().adminBooksRecyclerView.adapter = adapter

        viewModel.fetchBooks(schoolId = currentUser.schoolId)

        viewModel.observe(viewLifecycleOwner) { books -> adapter.books = books }

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

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}