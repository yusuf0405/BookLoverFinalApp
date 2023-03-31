package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.models.SavedStatus
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog.FragmentBookOptionDialogClickListeners
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.confim_dialog.FragmentConfirmDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.HorizontalItemsFingerprintSecond
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.MainScreenBookBlockFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.BookHorizontalFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints.HeaderFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.genre.GenreOnClickListener
import com.example.bookloverfinalapp.app.utils.genre.GenreTags
import com.example.bookloverfinalapp.databinding.FragmentBookInfoBinding
import com.joseph.ui_core.adapter.FingerprintAdapter
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.showBlurImage
import com.joseph.utils_core.extensions.showRoundedImage
import com.joseph.utils_core.motion.MotionListener
import com.joseph.utils_core.motion.MotionState
import com.joseph.utils_core.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FragmentBookInfo : BaseFragment<FragmentBookInfoBinding, FragmentBookInfoViewModel>(
    FragmentBookInfoBinding::inflate
), GenreOnClickListener, FragmentBookOptionDialogClickListeners {

    private val bookId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentBookInfoArgs.fromBundle(requireArguments()).bookId ?: String()
    }

    private var currentBook = Book.unknown()

    @Inject
    lateinit var factory: FragmentBookInfoViewModel.Factory
    override val viewModel: FragmentBookInfoViewModel by viewModelCreator {
        factory.create(bookId = bookId)
    }

    private val adapter = FingerprintAdapter(
        listOf(
            HeaderFingerprint(),
            MainScreenBookBlockFingerprint(
                listOf(BookHorizontalFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
            HeaderFingerprint(),
            HorizontalItemsFingerprintSecond(
                listOf(HorizontalUserFingerprint()),
                RecyclerView.RecycledViewPool()
            ),
        )
    )

    private val motionListener = MotionListener(::setToolbarState)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isFullScreen = true
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeResource()
    }

    private fun setupViews() = with(binding()) {
        root.addTransitionListener(motionListener)
        includeBookInfoBlock.bookGenresLayout.removeAllViews()
        includeBookInfoBlock.itemsRecyclerView.adapter = adapter
    }

    private fun setOnClickListeners() = with(binding()) {
        with(includeBookInfoToolbarBlock) {
            moreIcon.setOnDownEffectClickListener { showFragmentBookOptionDialog(currentBook.id) }
            saveIcon.setOnDownEffectClickListener { addOrDeleteBookInSavedBooks() }
            backIcon.setOnDownEffectClickListener { viewModel.navigateBack() }
        }
        with(includeBookInfoPosterBlock) {
            moreIcon.setOnDownEffectClickListener { showFragmentBookOptionDialog(currentBook.id) }
            saveIcon.setOnDownEffectClickListener { addOrDeleteBookInSavedBooks() }
            backIcon.setOnDownEffectClickListener { viewModel.navigateBack() }
        }
        includeBookInfoBlock.readButton.setOnDownEffectClickListener {
            viewModel.navigateToReadBookFragment()
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            bookFlow.observe(::handelBookFetching)
            reviewersFlow.observe(adapter::submitList)
            similarBooksFlow.observe(adapter::submitList)
            addOrDeleteOperationStatus.observe(::handleSuccessOperation)
            progressDialogIsShowingDialog.observe(::showAndDismissProgressDialog)
            genres.observe(::showGenres)
            showBookOptionDialogFlow.observe(::showFragmentBookOptionDialog)
            showSavedBookDeleteDialogFlow.observe { showConfirmDialog() }
        }
    }

    private fun handelBookFetching(book: Book) = with(binding()) {
        root.progress = EXPANDED
        viewModel.updateMotionPosition(EXPANDED)
        currentBook = book
        with(includeBookInfoPosterBlock) {
            bookTitle.text = book.title
            bookAuthor.text = book.author
        }
        with(includeBookInfoBlock) {
            nestedScroolView.smoothScrollTo(0, 0)
            bookPublicYear.text = book.publicYear
            bookPageCount.text = book.page.toString()
            bookChapterCount.text = book.chapterCount.toString()
            book.showDescription(
                textView = bookSubtitle,
                rootView = root,
                openFullDescription = { bookSubtitleScrollView.smoothScrollTo(0, 0) }
            )
        }
        includeBookInfoToolbarBlock.toolbarBookTitle.text = book.title
        handleBookIsSavedStatus(status = book.savedStatus)
        applyPosterImages(posterUrl = book.poster.url)
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> {
                viewModel.updateMotionPosition(COLLAPSED)
                binding().includeBookInfoBlock.nestedScroolView.smoothScrollTo(0, 0)
            }
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun handleSuccessOperation(savedStatus: SavedStatus) {
        handleBookIsSavedStatus(savedStatus)
        val message = if (savedStatus == SavedStatus.SAVED) getString(R.string.book_success_added)
        else getString(R.string.book_removed_successfully)
        showSuccessSnackBar(message)
    }

    private fun addOrDeleteBookInSavedBooks() {
        viewModel.addOrDeleteBookInSavedBooks()
    }

    private fun showAndDismissProgressDialog(isShow: Boolean) = Unit
//        if (isShow) progressDialog.showOnlyOne(parentFragmentManager)
//        else progressDialog.dismiss()

    private fun handleBookIsSavedStatus(status: SavedStatus) = with(binding()) {
        when (status) {
            SavedStatus.SAVING -> Unit
            SavedStatus.SAVED -> {
                setReadBookButtonVisibility(isVisible = true)
                setSaveIconInToolbarBlock(R.drawable.saved_single_icon)
                setSaveIconInPosterBlock(R.drawable.saved_single_white_icon)
            }
            SavedStatus.NOT_SAVED -> {
                setReadBookButtonVisibility(isVisible = false)
                setSaveIconInToolbarBlock(R.drawable.save_single_icon)
                setSaveIconInPosterBlock(R.drawable.save_single_white_icon)
            }
        }
    }

    private fun setSaveIconInToolbarBlock(@DrawableRes id: Int) = with(binding()) {
        val icon = ContextCompat.getDrawable(requireContext(), id)
        includeBookInfoToolbarBlock.saveIcon.setImageDrawable(icon)
    }

    private fun setSaveIconInPosterBlock(@DrawableRes id: Int) = with(binding()) {
        val icon = ContextCompat.getDrawable(requireContext(), id)
        includeBookInfoPosterBlock.saveIcon.setImageDrawable(icon)
    }

    private fun showFragmentBookOptionDialog(bookId: String) = FragmentBookOptionDialog
        .newInstance(bookId = bookId, listener = this)
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun applyPosterImages(posterUrl: String) {
        requireContext().showRoundedImage(
            imageUrl = posterUrl,
            imageView = binding().includeBookInfoPosterBlock.bookPoster
        )
        requireContext().showBlurImage(
            blurSize = BACKGROUND_IMAGE_BLUR_SIZE,
            imageUrl = posterUrl,
            imageView = binding().includeBookInfoPosterBlock.bookBlurBackgroundPoster
        )
    }


    private fun showGenres(
        genreList: List<Genre>
    ) = with(binding().includeBookInfoBlock.bookGenresLayout) {
        removeAllViews()
        genreList.forEach { genre -> addView(createGenreTag(genre)) }
    }

    private fun createGenreTag(genre: Genre) =
        GenreTags(context = requireContext(), actionListener = this@FragmentBookInfo)
            .getGenreTag(genre)

    private fun setReadBookButtonVisibility(isVisible: Boolean) {
        binding().includeBookInfoBlock.readButton.isVisible = isVisible
    }

    override fun genreItemOnClickListener(genre: Genre, textView: TextView, container: CardView) {
        viewModel.navigateToGenreInfoFragment(genreId = genre.id)
    }

    override fun addQuestionOnClickListener(bookId: String) {
        viewModel.navigateToCreateQuestionFragment()
    }

    override fun editBookOnClickListener(bookId: String) {
        viewModel.navigateToEditBookFragment()
    }

    override fun bookReadOnClickListener(savedBook: BookThatRead) {
        viewModel.navigateToReadBookFragment()
    }

    private fun showConfirmDialog() = FragmentConfirmDialog.newInstance(
        setOnPositiveButtonClickListener = { viewModel.startDeleteBookInSavedBooks() }
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

    override fun onDestroyView() {
        binding().root.removeTransitionListener(motionListener)
        super.onDestroyView()
    }

    private companion object {
        const val BACKGROUND_IMAGE_BLUR_SIZE = 25f
    }
}