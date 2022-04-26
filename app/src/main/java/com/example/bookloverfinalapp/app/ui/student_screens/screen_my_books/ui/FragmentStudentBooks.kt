package com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel
import com.example.bookloverfinalapp.app.models.StudentBookPdf
import com.example.bookloverfinalapp.app.models.StudentBookPoster
import com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.StudentBookAdapter
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.StudentBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.SwipeGesture
import com.example.bookloverfinalapp.databinding.FragmentStudentMyBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentStudentBooks : BaseFragment<FragmentStudentMyBooksBinding, StudentBooksViewModel>(
    FragmentStudentMyBooksBinding::inflate), StudentBookItemOnClickListener {

    override val viewModel: StudentBooksViewModel by viewModels()

    private val adapter: StudentBookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StudentBookAdapter(actionListener = this)
    }

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
    }

    private fun setupUi() {
        binding().apply {
            val touchHelper = ItemTouchHelper(swipeGesture())
            myBooksRecyclerView.adapter = adapter
            touchHelper.attachToRecyclerView(myBooksRecyclerView)
        }
    }

    private fun swipeGesture() = object : SwipeGesture(context = requireContext()) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when (direction) {
                ItemTouchHelper.LEFT -> deleteBook(position = viewHolder.adapterPosition)
            }
        }
    }

    private fun deleteBook(position: Int) {
        adapter.books[position].map(object : StudentBookAdapterModel.StudentStringMapper {
            override fun map(message: String) {}

            override fun map(
                author: String,
                createdAt: Date,
                bookId: String,
                objectId: String,
                page: Int,
                publicYear: String,
                title: String,
                chapterCount: Int,
                chaptersRead: Int,
                poster: StudentBookPoster,
                updatedAt: Date,
                book: StudentBookPdf,
                progress: Int,
                isReadingPages: List<Boolean>,
            ) {
                viewModel.deleteBook(id = objectId).observe(viewLifecycleOwner) {
                    adapter.deleteBook(position)
                    if (adapter.books.isEmpty()) viewModel.listIsEmpty()
                }
            }

        })
    }

    private fun observeResource() {
        viewModel.fetchMyBook(currentUser.id)
        viewModel.observe(viewLifecycleOwner) { books -> adapter.books = books.toMutableList() }
    }


    override fun tryAgain() {
        viewModel.fetchMyBook(currentUser.id)
    }

    override fun goChapterFragment(book: StudentBook) {
        findNavController()
            .navigate(FragmentRootStudentBookDirections.
            actionFragmentRootStudentBookToFragmentStudentChapterBook(book = book))
    }
}