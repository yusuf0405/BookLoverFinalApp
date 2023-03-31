package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.*
import com.joseph.utils_core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showRoundedImage
import com.example.bookloverfinalapp.databinding.FragmentBookOptionDialogBinding
import com.example.data.cache.models.IdResourceString
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.toDp
import com.joseph.utils_core.viewModelCreator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@AndroidEntryPoint
class FragmentBookOptionDialog : DialogFragment() {

    private var _binding: FragmentBookOptionDialogBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private val bookId: String by lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().getString(BOOK_KEY, String()) ?: String()
    }

    @Inject
    lateinit var factory: FragmentBookOptionDialogViewModel.Factory
    private val viewModel: FragmentBookOptionDialogViewModel by viewModelCreator {
        factory.create(bookId = bookId)
    }

    private var listener: FragmentBookOptionDialogClickListeners? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookOptionDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeResource()
    }

    private fun setOnClickListeners() = with(binding) {
        deleteBookBlock.setOnDownEffectClickListener {
            dismissModalPage()
            viewModel.startDeleteBookInSavedBooks()
        }
        saveBookBlock.setOnDownEffectClickListener {
            dismissModalPage()
            viewModel.startAddBookToSavedBooks()
        }
        readBookBlock.setOnDownEffectClickListener {
            dismissModalPage()
            val savedBook = viewModel.fetchSavedBook()
            if (savedBook == BookThatRead.unknown()) return@setOnDownEffectClickListener
            listener?.bookReadOnClickListener(savedBook = savedBook)
        }
        addQuestionBlock.setOnDownEffectClickListener {
            dismissModalPage()
            listener?.addQuestionOnClickListener(bookId)
        }
        editBookBlock.setOnDownEffectClickListener {
            showInfoSnackBar("Функция временно недоступно!")
            dismissModalPage()
            return@setOnDownEffectClickListener
            listener?.editBookOnClickListener(bookId = bookId)
        }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            bookFlow.observe(::handleBookFetching)
            isErrorNotification.observe { messageId ->
                showErrorSnackbar(messageId)
                dismiss()
            }
            isSuccessNotification.observe { messageId ->
                showSuccessSnackBar(messageId)
                dismiss()
            }
            currentUserFlow.filter { it != User.unknown() }
                .observe(::handleUserFetching)
        }
    }

    private fun handleBookFetching(book: Book) = with(binding) {
        requireContext().showRoundedImage(4.toDp, book.poster.url, poster)
        title.text = book.title
        author.text = book.author
        val deleteBookBlockIsVisible = book.savedStatus == SavedStatus.SAVED
        deleteBookBlock.isVisible = deleteBookBlockIsVisible
        saveBookBlock.isVisible = !deleteBookBlockIsVisible
        readBookBlock.isVisible = deleteBookBlockIsVisible
    }

    private fun handleUserFetching(user: User) = with(binding) {
        val userIsTeacher = user.userType == UserType.teacher
        editBookBlock.isVisible = userIsTeacher
        addQuestionBlock.isVisible = userIsTeacher
    }

    private fun showErrorSnackbar(messageId: IdResourceString) =
        GenericSnackbar
            .Builder(requireActivity().findViewById(R.id.rootContainer))
            .error()
            .message(messageId.format(requireContext()))
            .build()
            .show()

    private fun showSuccessSnackBar(messageId: IdResourceString) =
        GenericSnackbar
            .Builder(requireActivity().findViewById(R.id.rootContainer))
            .success()
            .message(messageId.format(requireContext()))
            .build()
            .show()

    private fun showInfoSnackBar(message: String) =
        GenericSnackbar
            .Builder(requireActivity().findViewById(R.id.rootContainer))
            .info()
            .message(message)
            .build()
            .show()


    companion object {
        private const val BOOK_KEY = "BOOK_KEY"

        @JvmStatic
        fun newInstance(
            bookId: String,
            listener: FragmentBookOptionDialogClickListeners
        ) = FragmentBookOptionDialog().run {
            this.listener = listener
            arguments = Bundle().apply { putString(BOOK_KEY, bookId) }
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}