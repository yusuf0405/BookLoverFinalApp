package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.ui.MotionListener
import com.example.bookloverfinalapp.app.ui.MotionState
import com.example.bookloverfinalapp.app.utils.cons.READ_EXTERNAL_STORAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.genre.GenreOnClickListener
import com.example.bookloverfinalapp.app.utils.genre.GenreTags
import com.example.bookloverfinalapp.databinding.FragmentAdminUploadPdfBinding
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.parse.ParseFile
import com.shockwave.pdfium.PdfDocument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FragmentUploadFile :
    BaseFragment<FragmentAdminUploadPdfBinding, FragmentUploadFileViewModel>(
        FragmentAdminUploadPdfBinding::inflate
    ), OnPageChangeListener, OnLoadCompleteListener, OnErrorListener, GenreOnClickListener {

    override val viewModel: FragmentUploadFileViewModel by viewModels()

    @Inject
    lateinit var bookPdfHelper: BookPdfHelper

    private val motionListener = MotionListener(::setToolbarState)

    private val uploadFileDialog: UploadBookDialog by lazy(LazyThreadSafetyMode.NONE) {
        UploadBookDialog.getInstance()
    }

    private val uploafFileType: UploadFileType by lazy(LazyThreadSafetyMode.NONE) {
        FragmentUploadFileArgs.fromBundle(requireArguments()).uploadType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupViews()
        setOnClickListeners()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        root.addTransitionListener(motionListener)
        requireContext().showRoundedImage(
            imageUrl = String(),
            imageView = bookUploadPoster
        )
    }

    private fun setOnClickListeners() = with(binding()) {
        downEffect(saveContainer).setOnClickListener { saveFiles() }
        downEffect(pickPdfFile).setOnClickListener { startPickPdfFileInStorage() }
        downEffect(upButtonSaveContainer).setOnClickListener { viewModel.navigateBack() }
        downEffect(upButton).setOnClickListener { viewModel.navigateBack() }
        downEffect(changeBookPoster).setOnClickListener { pickImageFileInStorage() }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            startAddNewBookFlow.observe { startAddNewBook() }
            fileUploadNotificationsShowingFlow.observe(::handleUploadFileDialogIsShowing)
            fileUploadProgressFlow.observe(uploadFileDialog::setTitle)
            allGenres.observe(::handleGenreFetching)
        }
    }

    private fun handleGenreFetching(genres: List<Genre>) {
        genres.forEach { genre ->
            binding().bookGenresLayout.addView(createGenreTag(genre))
        }
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> viewModel.updateMotionPosition(COLLAPSED)
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun startPickPdfFileInStorage() {
        if (!checkReadExternalStoragePermission()) return
        pickPdfFileInStorage()
    }

    private val resultPickReadExternalStorage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) return@registerForActivityResult
            pickPdfFileInStorage()
        }

    private fun pickImageFileInStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickBookPoster.launch(intent)
    }

    private fun checkReadExternalStoragePermission() = if (ContextCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        ) != PermissionChecker.PERMISSION_GRANTED
    ) {
        resultPickReadExternalStorage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        false
    } else true

    private fun pickPdfFileInStorage() {
        val intentType = when (uploafFileType) {
            UploadFileType.PDF_BOOK -> INTENT_PDF_TYPE
            UploadFileType.AUDIO_BOOK -> INTENT_AUDIO_TYPE
        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = intentType
            addCategory(Intent.CATEGORY_OPENABLE)
            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        resultPickPdfFile.launch(intent)
    }

    private val resultPickPdfFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val data: Intent? = result.data
            data?.data?.let(::handlePickPdfResult)
        }

    private val resultPickBookPoster =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val data: Intent? = result.data
            data?.data?.let(::handlePickPosterResult)
        }

    private fun takePersistableUriPermission(uri: Uri) = requireActivity()
        .contentResolver
        ?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

    private fun handlePickPdfResult(uri: Uri) {
        takePersistableUriPermission(uri)
        displayPdfViewFromUri(uri)
        generateImageFromPdf(uri)
        viewModel.updateBookPdfFlow(ParseFile(getFile(uri)))
    }

    private fun handlePickPosterResult(uri: Uri) {
        viewModel.updateBookPosterFlow(ParseFile(DEFAULT_IMAGE_TITLE, uriToImage(uri)))
        requireContext().showRoundedImage(
            roundedSize = POSTER_ROUNDED_SIZE,
            imageUri = uri,
            imageView = binding().bookUploadPoster
        )
    }

    private fun displayPdfViewFromUri(uri: Uri) {
        binding().pdfview.fromUri(uri)
            .onPageChange(this)
            .onLoad(this)
            .onError(this)
            .load()
    }

    private fun saveFiles() {
        val errorMessage = getString(R.string.fill_in_all_fields)
        when {
            fetchBookCurrentTitleFromInput().isBlank() -> showInfoSnackBar(errorMessage)
            fetchBookCurrentAuthorFromInput().isBlank() -> showInfoSnackBar(errorMessage)
            fetchBookCurrentPublicYearFromInput().isBlank() -> showInfoSnackBar(errorMessage)
            fetchBookCurrentSubtitleFromInput().isBlank() -> showInfoSnackBar(errorMessage)
            viewModel.fetchCheckedBookGenresListValue().isEmpty() -> showInfoSnackBar(errorMessage)
            else -> viewModel.startSaveNewBook()
        }
    }

    private fun startAddNewBook() {
        uploadFileDialog.dismiss()
        viewModel.addBook(
            description = fetchBookCurrentSubtitleFromInput(),
            author = fetchBookCurrentAuthorFromInput(),
            title = fetchBookCurrentTitleFromInput(),
            publicYear = fetchBookCurrentPublicYearFromInput()
        )
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        viewModel.updateBookPageCountFlow(pageCount)
    }

    private fun createGenreTag(genre: Genre) =
        GenreTags(context = requireContext(), actionListener = this).getGenreTag(genre)

    private fun generateImageFromPdf(pdfUri: Uri) = lifecycleScope.launch {
        async(Dispatchers.IO) {
            return@async bookPdfHelper.fetchBookPosterFromPdfUri(requireContext(), pdfUri)
        }.await().apply(::handleFetchPosterResult)
    }

    private fun handleFetchPosterResult(poster: Pair<Bitmap?, ByteArray?>) {
        poster.second?.let { byteArray ->
            viewModel.updateBookPosterFlow(ParseFile(DEFAULT_IMAGE_TITLE, byteArray))
        }
        poster.first?.let { bitmap ->
            requireContext().showRoundedImage(
                roundedSize = POSTER_ROUNDED_SIZE,
                imageBitmap = bitmap,
                imageView = binding().bookUploadPoster
            )
        }
    }

    private fun handleUploadFileDialogIsShowing(isShow: Boolean) {
        if (isShow) showUploadFileDialog()
        else uploadFileDialog.dismiss()
    }

    private fun showUploadFileDialog() = uploadFileDialog.showOnlyOne(parentFragmentManager)

    override fun loadComplete(nbPages: Int) {
        with(binding()) {
            changeBookPoster.show()
            pickPdfFile.isInvisible = true
            val meta: PdfDocument.Meta = pdfview.documentMeta
            setTextInBookTitleInput(meta.title)
            setTextInBookSubtitleInput(meta.subject)
            setTextInBookAuthorInput(meta.author)
            viewModel.updateBookChapterCountFlow(pdfview.tableOfContents.size)
        }
    }

    override fun onError(t: Throwable?) = Unit

    override fun genreItemOnClickListener(genre: Genre, textView: TextView, container: CardView) {

        if (viewModel.fetchCheckedBookGenresListValue().contains(genre)) {
            val backgroundColor = requireContext().getAttrColor(R.attr.whiteOrGrayColor)
            container.setCardBackgroundColor(backgroundColor)

            val textColor = requireContext().getAttrColor(R.attr.blackOrWhiteColor)
            textView.setTextColor(textColor)
            viewModel.removeNewGenreToCheckedBookGenresList(genre)
            return
        }
        viewModel.addNewGenreToCheckedBookGenresList(genre)
        val backgroundColor = requireContext().getAttrColor(R.attr.checked_item_color)
        container.setCardBackgroundColor(backgroundColor)

        val textColor = requireContext().getAttrColor(R.attr.whiteOrBlackColor)
        textView.setTextColor(textColor)
    }

    private fun fetchBookCurrentTitleFromInput() = binding().bookTitleInput.text()

    private fun fetchBookCurrentAuthorFromInput() = binding().bookAuthorInput.text()

    private fun fetchBookCurrentPublicYearFromInput() = binding().bookDatePublicationInput.text()

    private fun fetchBookCurrentSubtitleFromInput() = binding().bookSubtitleInput.text()

    private fun setTextInBookTitleInput(text: String) = binding().bookTitleInput.setText(text)

    private fun setTextInBookAuthorInput(text: String) = binding().bookAuthorInput.setText(text)

    private fun setTextInBookSubtitleInput(text: String) = binding().bookSubtitleInput.setText(text)

    override fun onStart() {
        super.onStart()
        binding().root.progress = viewModel.motionPosition.value
    }

    override fun onDestroyView() {
        binding().root.removeTransitionListener(motionListener)
        super.onDestroyView()
    }

    private companion object {
        const val INTENT_PDF_TYPE = "application/pdf"
        const val INTENT_AUDIO_TYPE = "audio/*"
        const val DEFAULT_IMAGE_TITLE = "image.png"
        const val POSTER_ROUNDED_SIZE = 8
    }
}

