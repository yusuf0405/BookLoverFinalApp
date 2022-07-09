package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentAllBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentAllBooks :
    BaseFragment<FragmentAllBooksBinding, FragmentAllBooksViewModel>(FragmentAllBooksBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener,
    ItemOnClickListener {

    override val viewModel: FragmentAllBooksViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    private val currentUserSchoolId by lazy(LazyThreadSafetyMode.NONE) { currentUser.schoolId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding()) {
            bookRecyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener(this@FragmentAllBooks)
            searchEditText.setOnQueryTextListener(this@FragmentAllBooks)
        }

        viewModel.fetchBooks(schoolId = currentUserSchoolId)

        viewModel.collect(viewLifecycleOwner) { books ->
            adapter.map(books.swapElements().toMutableList())
        }

    }

    override fun onRefresh() {
        viewModel.onRefresh(schoolId = currentUserSchoolId)
        binding().swipeRefresh.apply {
            isRefreshing = true
            postDelayed({ isRefreshing = false }, 1500)
        }
    }

    override fun onQueryTextSubmit(searchText: String?): Boolean {
        if (searchText != null) viewModel.searchBook(searchText, schoolId = currentUserSchoolId)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            val searchText = newText.lowercase(Locale.getDefault())
            viewModel.searchBook(searchText, schoolId = currentUserSchoolId)
        } else viewModel.fetchBooks(schoolId = currentUserSchoolId)
        return false
    }

    override fun showAnotherFragment(item: ItemUi) {
        viewModel.goStudentBookDetailsFragment(book = viewModel.mapper.map(item as BookModel))
    }

}