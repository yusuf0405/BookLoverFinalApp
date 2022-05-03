package com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.StudentBookAdapter
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.StudentBookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.student_screens.screen_my_books.adapters.SwipeGesture
import com.example.bookloverfinalapp.databinding.FragmentMyBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@AndroidEntryPoint
class FragmentMyBooks : BaseFragment<FragmentMyBooksBinding, MyBooksViewModel>(
    FragmentMyBooksBinding::inflate), StudentBookItemOnClickListener {

    override val viewModel: MyBooksViewModel by viewModels()

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
        adapter.bookThatReads[position].map(object :
            BookThatReadAdapterModel.BookThatReadStringMapper {
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
                poster: BookThatReadPoster,
                updatedAt: Date,
                book: String,
                progress: Int,
                isReadingPages: List<Boolean>,
            ) {
                viewModel.deleteBook(id = objectId).observe(viewLifecycleOwner) {
                    adapter.deleteBook(position)
                    if (adapter.bookThatReads.isEmpty()) viewModel.listIsEmpty()
                }
                File(book).delete()
            }

        })
    }

    private fun observeResource() {
        viewModel.fetchMyBook(currentUser.id)
        viewModel.observe(viewLifecycleOwner) { books ->
            adapter.bookThatReads = books.toMutableList()
        }
    }


    override fun tryAgain() {
        viewModel.fetchMyBook(currentUser.id)
    }

    override fun goChapterFragment(book: BookThatRead) {
        findNavController().navigate(FragmentRootStudentBookDirections.actionFragmentRootStudentBookToFragmentChapterBook(
            book = book))
    }
}