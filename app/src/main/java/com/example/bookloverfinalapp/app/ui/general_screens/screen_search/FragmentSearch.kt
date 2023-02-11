package com.example.bookloverfinalapp.app.ui.general_screens.screen_search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.HorizontalUserFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.*
import com.example.bookloverfinalapp.app.ui.player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.setupTextSize
import com.example.bookloverfinalapp.databinding.FragmentSearchBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter


@AndroidEntryPoint
class FragmentSearch :
    BaseFragment<FragmentSearchBinding, FragmentSearchViewModel>(FragmentSearchBinding::inflate),
    SearchView.OnQueryTextListener, FragmentBookOptionDialogClickListeners {

    override val viewModel: FragmentSearchViewModel by viewModels()

    private val adapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            HorizontalItemsFingerprintSecond(
                listOf(TaskFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenAudioBookBlockFingerprint(
                listOf(AudioBookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()

            ),
            HeaderFingerprint(),
            HorizontalItemsFingerprint(
                listOf(SavedBookMainFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenBookBlockFingerprint(
                listOf(BookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            MainScreenUserBlockFingerprint(
                listOf(HorizontalUserFingerprint()),
                RecyclerView.RecycledViewPool(),
            ),
        )
    )

    private var playerCallback: PlayerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCallback = context as? PlayerCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        itemSearchView.searchHeader.setupTextSize()
        upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
        recyclerView.adapter = adapter
        itemSearchView.searchHeader.setOnQueryTextListener(this@FragmentSearch)
        itemSearchView.searchHeader.queryHint =
            getString(R.string.title_of_the_book_name_of_the_author_or_user)
    }

    private fun observeResource() = with(viewModel) {
        launchWhenStarted {
            allFilteredItemsFlow.filter { it.isNotEmpty() }.observe(::populateItems)
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            playAudioBookFlow.observe { playerCallback?.play(it) }
        }
    }

    private fun populateItems(items: List<Item>) {
        adapter.submitList(items)
    }

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    override fun onQueryTextSubmit(searchString: String?): Boolean {
        if (searchString != null) viewModel.updateSearchQuery(searchString = searchString)
        return false
    }

    override fun onQueryTextChange(searchString: String?): Boolean {
        if (searchString != null) viewModel.updateSearchQuery(searchString = searchString)
        return false
    }

    override fun addQuestionOnClickListener(bookId: String) {
        viewModel.navigateToCreateQuestionFragment(bookId)
    }

    override fun editBookOnClickListener(bookId: String) {
        viewModel.navigateToEditBookFragment(bookId)
    }

    override fun bookReadOnClickListener(savedBook: BookThatRead) {
        viewModel.navigateToBookReadBlockFragment(savedBook)
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }
}