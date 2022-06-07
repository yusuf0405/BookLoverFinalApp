package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.bookloverfinalapp.app.ui.adapter.BookModel
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.genre.GenreOnClickListener
import com.example.bookloverfinalapp.app.utils.genre.GenreTags
import com.example.bookloverfinalapp.app.utils.genre.GenresManager
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.databinding.FragmentBookInfoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentBookInfo : BaseFragment<FragmentBookInfoBinding, FragmentBookInfoViewModel>(
    FragmentBookInfoBinding::inflate), View.OnClickListener,
    GenreOnClickListener, ItemOnClickListener {

    override val viewModel: FragmentBookInfoViewModel by viewModels()

    private lateinit var book: Book

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
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
                if (NavigationManager().isOnline(requireContext())) addMyBooks()
                else showSnackbar(binding().root, getString(R.string.network_error))
            }
        }
    }

    private fun observeResource() {
        if (NavigationManager().isOnline(requireContext())) viewModel.chekIsMyBook(id = book.objectId,
            userId = currentUser.id).observe(viewLifecycleOwner) { progress ->
            binding().bookProgress.setProgress(progress)
        }

        viewModel.fetchMyBook(id = currentUser.id)

        viewModel.collect(viewLifecycleOwner) { books -> adapter.map(books.toMutableList()) }

        book.genres.forEach { genreCode ->
            binding().bookGenresLayout.addView(
                GenreTags(context = requireContext(),
                    actionListener = this).getGenreTag(genreName =
                GenresManager.getGenreByCloudCode(
                    context = requireContext(),
                    code = genreCode),
                    genreCode = genreCode)
            )
        }
        viewModel.collectProgressAnimation(viewLifecycleOwner) {
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
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }

    override fun genreOnClick(title: String) {
        viewModel.goGenreSimilarBooks(genre = title)
    }

    override fun showAnotherFragment(item: ItemUi) {
        val bookUi = item as BookModel
        book = viewModel.adapterMapper.map(bookUi)
        lifecycleScope.launch {
            binding().addButton.apply {
                if (visibility == View.GONE) visibility = View.VISIBLE
                playAnimation()
                delay(20)
                cancelAnimation()
                isClickable = true
            }
        }
        setupUi()
    }
}