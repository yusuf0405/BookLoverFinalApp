package com.example.bookloverfinalapp.app.ui.general_screens.screen_search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.confim_dialog.FragmentConfirmDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details.HorizontalUserFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.*
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.*
import com.example.bookloverfinalapp.app.ui.service_player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.setupSearchViewParams
import com.example.bookloverfinalapp.databinding.FragmentSearchBinding
import com.joseph.ui.core.adapter.FingerprintAdapter
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.extensions.launchWhenViewStarted
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
                listOf(HorizontalUserFingerprint(isWrapContent = true)),
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
        itemSearchView.searchHeader.setupSearchViewParams()
        upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
        recyclerView.adapter = adapter
        itemSearchView.searchHeader.setOnQueryTextListener(this@FragmentSearch)
        val hint = getString(R.string.search)
        itemSearchView.searchHeader.queryHint = hint
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            allFilteredItemsFlow.filter { it.isNotEmpty() }.observe(::populateItems)
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            playAudioBookFlow.observe { playerCallback?.play(it) }
            showSavedBookDeleteDialogFlow.observe(::showConfirmDialogFlow)
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

    private fun showConfirmDialogFlow(id: String) {
        FragmentConfirmDialog.newInstance(
            setOnPositiveButtonClickListener = { viewModel.deleteBookInSavedBooks(id) }
        ).show(requireActivity().supportFragmentManager, ModalPage.TAG)
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }
}