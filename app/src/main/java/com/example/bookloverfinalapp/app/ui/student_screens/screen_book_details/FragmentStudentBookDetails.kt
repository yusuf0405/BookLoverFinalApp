package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.launchWhenStarted
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentBookDetailsBinding
import com.example.domain.models.book.AddNewBookRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentStudentBookDetails :
    BaseFragment<FragmentStudentBookDetailsBinding, FragmentStudentBookDetailsViewModel>(
        FragmentStudentBookDetailsBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentStudentBookDetailsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val book: Book by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentBookDetailsArgs.fromBundle(requireArguments()).book
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
        setOnClickListeners()
    }

    private fun addMyBooks() {
        val isReading = arrayListOf<Boolean>()
        isReading.add(true)
        for (i in 1 until book.chapterCount) isReading.add(false)
        val book = AddNewBookRequest(
            progress = 0,
            bookId = book.objectId,
            userId = currentUser.id,
            isReadingPages = isReading,
            chaptersRead = 0
        )
        viewModel.addNewBook(book = book)
    }

    private fun setOnClickListeners() {
        binding().apply {
            addMyBook.setOnClickListener(this@FragmentStudentBookDetails)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().addMyBook -> {
                binding().addMyBook.hideView()
                progressBarVisibility(status = true)
                addMyBooks()
            }
        }
    }

    private fun observeResource() {
        viewModel.observeProgressAnimation(viewLifecycleOwner) {
            it.getValue()?.let { status ->
                progressBarVisibility(status = status)

            }
        }
        viewModel.chekIsMyBook(userId = currentUser.id, bookId = book.objectId)
            .observe(viewLifecycleOwner) { book ->
                binding().apply {
                    bookDetailsReadChapters.text = book.chaptersRead.toString()
                    bookDetailsReadPages.text = book.progress.toString()
                }
            }
        viewModel.addBookState.onEach {
            showToast(message = getString(R.string.book_success_added))
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun progressBarVisibility(status: Boolean) {
        binding().apply {
            if (status) {
                imageView6.showView()
                imageView7.showView()
                textView22.showView()
                textView23.showView()
                bookDetailsReadPages.showView()
                bookDetailsReadChapters.showView()
                progressBar.hideView()
            } else {
                addMyBook.showView()
                progressBar.hideView()
            }
        }
    }

    private fun setupUi() {
        binding().apply {
            bookDetailsChapterCount.text = book.chapterCount.toString()
            bookDetailsTitle.text = book.title
            bookDetailsAutor.text = book.author
            bookDetailsPublicYear.text = book.publicYear
            bookDetailsCount.text = book.page.toString()
            toolbar.apply {
                title = book.title
                setTitleTextColor(Color.WHITE)
                setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            }
            Glide.with(requireActivity())
                .load(book.poster.url)
                .into(bookDetailsPoster)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}