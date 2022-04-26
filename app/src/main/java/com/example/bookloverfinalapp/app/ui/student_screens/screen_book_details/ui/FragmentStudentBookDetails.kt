package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_details.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.StudentBookPdf
import com.example.bookloverfinalapp.app.models.StudentBookPoster
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentBookDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

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

        val book = AddNewBookModel(
            bookId = book.objectId,
            page = book.page,
            publicYear = book.publicYear,
            book = StudentBookPdf(url = book.book.url, name = book.book.name),
            title = book.title,
            chapterCount = book.chapterCount,
            poster = StudentBookPoster(url = book.poster.url, name = book.poster.name),
            isReadingPages = isReading,
            chaptersRead = 0,
            progress = 0,
            author = book.author,
            userId = currentUser.id)

        viewModel.addNewBook(book = book).observe(viewLifecycleOwner) {
            binding().addMyBook.hideView()
            uiVisibility(status = true)
            showToast(getString(R.string.book_added_successfully))
        }
    }

    private fun setOnClickListeners() {
        binding().apply {
            addMyBook.setOnClickListener(this@FragmentStudentBookDetails)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().addMyBook -> addMyBooks()

        }
    }

    private fun observeResource() {
        viewModel.observeProgressAnimation(viewLifecycleOwner) {
            it.getValue()?.let { status ->
                uiVisibility(status = status)

            }
        }
        viewModel.chekIsMyBook(id = book.objectId)
            .observe(viewLifecycleOwner) { book ->
                binding().apply {
                    bookDetailsReadChapters.text = book.chaptersRead.toString()
                    bookDetailsReadPages.text = book.progress.toString()
                }
            }
    }

    private fun uiVisibility(status: Boolean) {
        binding().apply {
            if (status) {
                imageView6.showView()
                imageView7.showView()
                textView22.showView()
                textView23.showView()
                bookDetailsReadPages.showView()
                bookDetailsReadChapters.showView()
            } else {
                addMyBook.showView()
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