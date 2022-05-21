package com.example.bookloverfinalapp.app.ui.screen_book_details.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.bookloverfinalapp.app.ui.screen_book_details.adapters.BookItemOnClickListener
import com.example.bookloverfinalapp.app.ui.screen_book_details.adapters.SimilarBookAdapter
import com.example.bookloverfinalapp.app.utils.GenresManager
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.tags.GenreTags
import com.example.bookloverfinalapp.databinding.FragmentBookInfoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentBookInfo : BaseFragment<FragmentBookInfoBinding, FragmentBookInfoViewModel>(
    FragmentBookInfoBinding::inflate), View.OnClickListener, BookItemOnClickListener {

    override val viewModel: FragmentBookInfoViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private lateinit var book: Book

    private val adapter: SimilarBookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SimilarBookAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        book = FragmentBookInfoArgs.fromBundle(requireArguments()).book
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
            book = book.book.url,
            title = book.title,
            chapterCount = book.chapterCount,
            poster = BookThatReadPoster(url = book.poster.url, name = book.poster.name),
            isReadingPages = isReading,
            chaptersRead = 0,
            progress = 0,
            author = book.author,
            userId = currentUser.id)

        viewModel.addNewBook(book = book).observe(viewLifecycleOwner) {
            binding().addButton.apply {
                lifecycleScope.launch {
                    playAnimation()
                    delay(2000)
                    cancelAnimation()
                    isClickable = false
                }
            }

        }
    }

    private fun setOnClickListeners() {
        binding().apply {
            addButton.setOnClickListener(this@FragmentBookInfo)
            titleBar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().addButton -> {
                if (CheсkNavigation().isOnline(requireContext())) addMyBooks()
                else showSnackbar(binding().root, getString(R.string.network_error))
            }
        }
    }

    private fun observeResource() {
        viewModel.fetchMyBook(id = currentUser.id)

        viewModel.observe(viewLifecycleOwner) { books -> adapter.books = books }

        book.genres.forEach {
            binding().bookGenresLayout.addView(
                GenreTags(context = requireContext()).getGenreTag(genreName = GenresManager.getGenreByCloudCode(
                    context = requireContext(),
                    code = it))
            )
        }
        viewModel.observeProgressAnimation(viewLifecycleOwner) {
            it.getValue()?.let { status -> uiVisibility(status = status) }
        }
    }

    private fun uiVisibility(status: Boolean) {
        binding().apply {
            if (status) {
                addButton.hideView()
                bookProgress.showView()
            } else {
                bookProgress.hideView()
                addButton.showView()
            }

        }
    }

    private fun setupUi() {
        binding().apply {
            viewModel.fetchSimilarBook(genres = book.genres, bookId = book.objectId)
            setToolbarColor(toolbar = titleBar)
            rlvRecommend.adapter = adapter
            bookProgress.setTotal(book.page - 1)
            tvChapterCount.text = book.chapterCount.toString()
            tvBookName.text = book.title
            tvBookAuthor.text = book.author
            tvPublicYear.text = book.publicYear
            tvPageCount.text = book.page.toString()
            requireContext().glide(book.poster.url, tvBookImage)
            bookProgress.hideView()
        }

        viewModel.chekIsMyBook(id = book.objectId, userId = currentUser.id)
            .observe(viewLifecycleOwner) { progress ->
                binding().bookProgress.setProgress(progress)
            }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }

    override fun tryAgain() {}

    override fun goChapterBookFragment(book: Book) {
        this.book = book
        lifecycleScope.launch {
            binding().addButton.apply {
                playAnimation()
                delay(20)
                cancelAnimation()
                isClickable = true
            }
        }
        setupUi()


    }
}