package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_details.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showSnackbar
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.databinding.FragmentBookDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class FragmentBookDetails :
    BaseFragment<FragmentBookDetailsBinding, FragmentBookDetailsViewModel>(
        FragmentBookDetailsBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentBookDetailsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    lateinit var book: Book

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        book = FragmentBookDetailsArgs.fromBundle(requireArguments()).book
        setupUi()
        observeResource()
        setOnClickListeners()
    }

    private fun addMyBooks(path: String) {
        val isReading = arrayListOf<Boolean>()
        isReading.add(true)
        for (i in 1 until book.chapterCount) isReading.add(false)
        val book = AddNewBookModel(
            bookId = book.objectId,
            page = book.page,
            publicYear = book.publicYear,
            book = path,
            title = book.title,
            chapterCount = book.chapterCount,
            poster = BookThatReadPoster(url = book.poster.url, name = book.poster.name),
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

    private fun saveBook() =
        viewModel.go(book.book.url).observe(viewLifecycleOwner) { inputStream ->
            lifecycleScope.launch { addMyBooks(path = inputStream.saveToFile()) }
        }


    private fun setOnClickListeners() {
        binding().apply {
            addMyBook.setOnClickListener(this@FragmentBookDetails)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().addMyBook -> {
                if (CheсkNavigation().isOnline(requireContext())) saveBook()
                else showSnackbar(binding().root, getString(R.string.network_error))
            }
        }
    }

    private fun observeResource() {
        viewModel.observeProgressAnimation(viewLifecycleOwner) {
            it.getValue()?.let { status -> uiVisibility(status = status) }
        }

        viewModel.chekIsMyBook(id = book.objectId).observe(viewLifecycleOwner) { book ->
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
            } else addMyBook.showView()

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

    private suspend fun InputStream.saveToFile(): String {
        Log.i("Start", "Start")
        val path = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File.createTempFile("my_file", ".pdf", path)
        val result = withContext(Dispatchers.Default) {
            val save = async {
                return@async use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                }
            }
            save.await()
            return@withContext file.path
        }
        Log.i("End", "End")
        return result
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }

}