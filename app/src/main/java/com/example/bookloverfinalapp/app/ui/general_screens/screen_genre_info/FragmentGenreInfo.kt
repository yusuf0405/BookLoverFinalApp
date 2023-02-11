package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.adapter.animations.AddableItemAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SimpleCommonAnimator
import com.example.bookloverfinalapp.app.ui.adapter.animations.custom.SlideInTopCommonAnimator
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.FingerprintAdapter
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenAudioBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.AudioBookHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.SearchFingerprint
import com.example.bookloverfinalapp.app.ui.player.PlayerCallback
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showImage
import com.example.bookloverfinalapp.app.utils.genre.checkLanguageAndGetActualString
import com.example.bookloverfinalapp.databinding.FragmentGenreInfoBinding
import com.example.domain.models.GenreDomain
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentGenreInfo :
    BaseFragment<FragmentGenreInfoBinding, FragmentGenreInfoViewModel>(FragmentGenreInfoBinding::inflate),
    FragmentBookOptionDialogClickListeners {

    @Inject
    lateinit var factory: FragmentGenreInfoViewModelFactory.Factory

    private val genreId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentGenreInfoArgs.fromBundle(requireArguments()).genreId
    }

    override val viewModel: FragmentGenreInfoViewModel by viewModels {
        factory.create(genreId)
    }

    private val backgroundImages: List<Int> by lazy(LazyThreadSafetyMode.NONE) {
        listOf(
            R.drawable.genre_background_first,
            R.drawable.genre_background_second,
            R.drawable.genre_background_third,
            R.drawable.genre_background_fouth,
            R.drawable.genre_background_fifth,
            R.drawable.genre_background_sixth,
            R.drawable.genre_background_seventh,
        )
    }

    private val bookAdapter = FingerprintAdapter(
        listOf(
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
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        booksRecyclerView.adapter = bookAdapter
        booksRecyclerView.itemAnimator = createAddableItemAnimator()
    }

    private fun setOnClickListeners() = with(binding()) {
        includeGenreInfoToolbarBlock.upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
        includeGenreInfoPosterBlock.upButton.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            genre.observe(::handleGenre)
            fetchGenreBooks(genreId).observe(bookAdapter::submitList)
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