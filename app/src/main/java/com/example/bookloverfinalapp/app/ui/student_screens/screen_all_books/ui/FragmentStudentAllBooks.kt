package com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.adapters.BookAdapter
import com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.adapters.BookItemOnClickListener
import com.example.bookloverfinalapp.databinding.FragmentStudentAllBooksBinding
import com.example.data.data.cache.db.BooksDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentStudentAllBooks :
    BaseFragment<FragmentStudentAllBooksBinding, FragmentStudentAllBooksViewModel>(
        FragmentStudentAllBooksBinding::inflate),
    BookItemOnClickListener {
    override fun onReady(savedInstanceState: Bundle?) {}

    override val viewModel: FragmentStudentAllBooksViewModel by viewModels()



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
        viewModel.goBookDetailsFragment(book = book)
    }


}