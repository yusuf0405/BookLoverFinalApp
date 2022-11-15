package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.base.GenericAdapter
import com.example.bookloverfinalapp.app.base.ItemOnClickListener
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.app.utils.genre.GenresManager
import com.example.bookloverfinalapp.databinding.FragmentGenreSimilarBooksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentGenreSimilarBooks :
    BaseFragment<FragmentGenreSimilarBooksBinding, FragmentGenreSimilarBooksViewModel>(
        FragmentGenreSimilarBooksBinding::inflate), ItemOnClickListener {

    override val viewModel: FragmentGenreSimilarBooksViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }
    private val genre: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentGenreSimilarBooksArgs.fromBundle(requireArguments()).genre
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding()) {
            bookRecyclerView.adapter = adapter
            setToolbarColor(toolbar = titleBar)
            titleBar.title = GenresManager.getGenreByCloudCode(
                context = requireContext(),
                code = genre)
            titleBar.setNavigationOnClickListener { viewModel.goBack() }
        }

        viewModel.fetchSimilarBook(genre = genre)

        viewModel.collect(viewLifecycleOwner) { books ->
            adapter.map(books.swapElements().toMutableList())
        }
    }

    override fun showAnotherFragment(item: ItemUi) {
        val bookUi = item as BookModel
        val book = viewModel.adapterMapper.map(bookUi)
        viewModel.goBookInfoFragment(book = book)
    }
}