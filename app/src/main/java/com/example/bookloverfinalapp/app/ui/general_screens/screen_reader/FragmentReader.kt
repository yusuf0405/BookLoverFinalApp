package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.databinding.DialogPageEnterBinding
import com.example.bookloverfinalapp.databinding.FragmentReaderBinding
import com.github.barteksc.pdfviewer.listener.OnDrawListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class FragmentReader :
    BaseFragment<FragmentReaderBinding, FragmentReaderViewModel>(FragmentReaderBinding::inflate),
    OnLoadCompleteListener, OnErrorListener, OnPageChangeListener,
    View.OnClickListener {
    override val viewModel: FragmentReaderViewModel by viewModels()

    private val book: BookThatRead by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).book
    }
    private val startPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).startPage
    }
    private val lastPage: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).lastPage
    }
    private val chapter: Int by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).chapter
    }
    private val path: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentReaderArgs.fromBundle(requireArguments()).path
    }
    private val pdfPages = arrayListOf<Int>()
    private val pages = arrayListOf<Int>()
    private var progress = 0
    private var bookCurrentProgress = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeResource()
    }


    private fun observeResource() {
        loadSuccess()
    }

    private fun loadSuccess() {
        bookCurrentProgress = book.progress
        for (i in startPage until lastPage) {
            pdfPages.add(i)
        }

        val pageCount = lastPage - startPage
        for (i in 0 until pageCount) {
            val page = startPage + i
            pages.add(page)

        }
        binding().pdfview.fromFile(File(path))
            .spacing(100)
            .pages(*pdfPages.toIntArray())
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .onPageChange(this)
            .onLoad(this)
            .enableAnnotationRendering(true)
            .password(null)
            .scrollHandle(null)
            .load()
    }


    private fun setOnClickListeners() {
        binding().apply {
            downEffect(pageEnterField).setOnClickListener(this@FragmentReader)
            downEffect(endRead).setOnClickListener(this@FragmentReader)
            downEffect(pageLast).setOnClickListener(this@FragmentReader)
            downEffect(pageFirst).setOnClickListener(this@FragmentReader)
            downEffect(pageForward).setOnClickListener(this@FragmentReader)
            downEffect(pageBack).setOnClickListener(this@FragmentReader)
        }
    }

    override fun loadComplete(nbPages: Int) {}

    override fun onError(t: Throwable?) {
        showToast(R.string.generic_error)
        viewModel.goBack()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        binding().apply {
            pageEnterField.text = pdfPages[page].toString()
            if (page == pageCount - 1) {
                if (bookCurrentProgress <= progress) endRead.showView()
            } else endRead.hideView()

            progress = pdfPages[page]
        }

    }

    private fun saveChanges() {
        if (bookCurrentProgress < progress) {
            viewModel.updateProgress(id = book.objectId, progress = progress)
            bookCurrentProgress = progress
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().endRead -> {
                if (NavigationManager().isOnline(context = requireContext())) viewModel.goQuestionFragment(
                    book = book,
                    chapter = chapter,
                    path = path)
                else showToast(R.string.network_error)
            }
            binding().pageBack -> binding().pdfview.jumpTo(binding().pdfview.currentPage - 1)
            binding().pageForward -> binding().pdfview.jumpTo(binding().pdfview.currentPage + 1)
            binding().pageFirst -> binding().pdfview.jumpTo(0)
            binding().pageLast -> {
                binding().pdfview.jumpTo(pdfPages.size)
                if (bookCurrentProgress <= progress) binding().endRead.showView()
            }
            binding().pageEnterField -> showCustomInputAlertDialog(text = binding().pageEnterField.text.toString())

        }
    }

    private fun showCustomInputAlertDialog(text: String) {
        val dialogBinding = DialogPageEnterBinding.inflate(layoutInflater)
        val dialogTitle = "${getString(R.string.go_to_pages)} $startPage ... ${lastPage - 1}"
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.action_confirm, null)
            .create()
        dialogBinding.questionInputEditText.setText(text)
        dialog.setOnShowListener {
            dialogBinding.questionInputEditText.requestFocus()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogBinding.questionInputEditText.text.toString().trim()
                if (enteredText.isBlank()) {
                    dialogBinding.questionInputEditText.error = getString(R.string.empty_value)
                    return@setOnClickListener
                } else {
                    val newPage = enteredText.toInt()
                    if (newPage in startPage..lastPage) {
                        pages.forEach { oldPage ->
                            if (oldPage == newPage)
                                binding().pdfview.jumpTo(pages.indexOf(oldPage))
                        }
                        dialog.dismiss()
                    } else showToast(messageRes = R.string.have_gone_beyond)
                }

            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        saveChanges()
    }

}




