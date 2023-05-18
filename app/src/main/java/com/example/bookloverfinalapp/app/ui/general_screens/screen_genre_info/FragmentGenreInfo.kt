package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenAudioBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.*
import com.example.bookloverfinalapp.app.ui.service_player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.app.utils.genre.checkLanguageAndGetActualString
import com.example.bookloverfinalapp.databinding.FragmentGenreInfoBinding
import com.example.domain.models.GenreDomain
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.assistedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentGenreInfo :
    BaseFragment<FragmentGenreInfoBinding, FragmentGenreInfoViewModel>(FragmentGenreInfoBinding::inflate),
    FragmentBookOptionDialogClickListeners {



    private val genreId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentGenreInfoArgs.fromBundle(requireArguments()).genreId
    }

    @Inject
    lateinit var factory: FragmentGenreInfoViewModel.Factory
    override val viewModel: FragmentGenreInfoViewModel by assistedViewModel {
        factory.create(genreId)
    }

    private val adapter = FingerprintAdapter(
        listOf(
            EmptyDataFingerprint(),
            SearchFingerprint(),
            HeaderFingerprint(),
            MainScreenBookBlockFingerprint(
                listOf(BookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()

            ),
            HeaderFingerprint(),
            MainScreenAudioBookBlockFingerprint(
                listOf(AudioBookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()

            ),
        )
    )

    private var playerCallback: PlayerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerCallback = context as? PlayerCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        booksRecyclerView.adapter = adapter
        booksRecyclerView.itemAnimator = createAddableItemAnimator()
    }

    private fun setOnClickListeners() = with(binding()) {
        includeGenreInfoToolbarBlock.upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
        includeGenreInfoPosterBlock.upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            genre.observe(::handleGenre)
            fetchGenreBooks(genreId).observe(adapter::submitList)
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            playAudioBookFlow.observe { audioBookId ->
                playerCallback?.play(audioBookId = audioBookId)
            }
        }
    }

    private fun handleGenre(genre: GenreDomain) = with(binding()) {
        requireContext().showImage(
            genre.poster.url,
            includeGenreInfoPosterBlock.genreBackgroundImage
        )
        genreDescription.text = checkLanguageAndGetActualString(genre.descriptions)
        val title = checkLanguageAndGetActualString(genre.titles)
        includeGenreInfoPosterBlock.genreTitle.text = title
        includeGenreInfoToolbarBlock.genreTitle.text = title
    }

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun createAddableItemAnimator() =
        AddableItemAnimator(SimpleCommonAnimator()).also { anim ->
            anim.addViewTypeAnimation(
                R.layout.item_search_view,
                SlideInTopCommonAnimator()
            )
            anim.addDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
            anim.removeDuration = DEFAULT_ITEMS_ANIMATOR_DURATION
        }

    override fun addQuestionOnClickListener(bookId: String) {
        viewModel.navigateToCreateQuestionFragment(bookId = bookId)
    }

    override fun editBookOnClickListener(bookId: String) {
        viewModel.navigateToEditBookFragment(bookId = bookId)
    }

    override fun bookReadOnClickListener(savedBook: BookThatRead) {
        viewModel.navigateToBookReadFragment(savedBook = savedBook)
    }

    override fun onDetach() {
        super.onDetach()
        playerCallback = null
    }
}