package com.example.bookloverfinalapp.app.ui.screen_all_books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.screen_all_books.adapters.BookAdapter
import com.example.bookloverfinalapp.app.ui.screen_all_books.adapters.BookItemOnClickListener
import com.example.bookloverfinalapp.databinding.FragmentAllBooksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllBooks :
    BaseFragment<FragmentAllBooksBinding, FragmentAllBooksViewModel>(
        FragmentAllBooksBinding::inflate),
    BookItemOnClickListener {
    override fun onReady(savedInstanceState: Bundle?) {}

    override val viewModel: FragmentAllBooksViewModel by viewModels()


    private val adapter: BookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        BookAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().bookRecyclerView.adapter = adapter

        viewModel.fetchBooks()

        viewModel.observe(viewLifecycleOwner) { books -> adapter.books = books }

    }

    override fun tryAgain() {
        viewModel.fetchBooks()
    }

    override fun goChapterBookFragment(book: Book) {
        viewModel.goStudentBookDetailsFragment(book = book)
    }


}