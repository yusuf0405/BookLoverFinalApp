package com.example.bookloverfinalapp.app.ui.screen_my_books.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.screen_main_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.ui.screen_my_books.adapters.StudentBookAdapter
import com.example.bookloverfinalapp.app.ui.screen_my_books.adapters.StudentBookItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.createNewPath
import com.example.bookloverfinalapp.app.utils.extensions.getShPrString
import com.example.bookloverfinalapp.databinding.FragmentMyBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FragmentMyBooks : BaseFragment<FragmentMyBooksBinding, MyBooksViewModel>(
    FragmentMyBooksBinding::inflate), StudentBookItemOnClickListener {

    override val viewModel: MyBooksViewModel by viewModels()

    private val adapter: StudentBookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StudentBookAdapter(actionListener = this)
    }

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding().myBooksRecyclerView.adapter = adapter
        observeResource()
    }

    private fun observeResource() {
        viewModel.fetchMyBook(currentUser.id)

        viewModel.observe(viewLifecycleOwner) { books ->
            adapter.bookThatReads = books.toMutableList()
        }

        viewModel.bookObserve(viewLifecycleOwner) { books ->
            books.forEach { book ->
                val path = requireActivity().getShPrString(book.objectId)
                if (path == null)
                    viewModel.getBookPdf(book.book).observe(viewLifecycleOwner) { inputStream ->
                        createNewPath(inputStream = inputStream, key = book.objectId)
                    }
            }
        }
    }

    override fun tryAgain() {
        viewModel.fetchMyBook(currentUser.id)
    }

    override fun goChapterFragment(book: BookThatRead) {
        val path = requireActivity().getShPrString(book.objectId)
        if (path == null) showToast(R.string.book_is_not_ready)
        else findNavController().navigate(FragmentRootStudentBookDirections
            .actionFragmentRootBookToFragmentChapterBook(book = book, path = path))
    }


    override fun deleteBook(id: String, position: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> viewModel.deleteBook(id = id)
                    .observe(viewLifecycleOwner) {
                        adapter.deleteBook(position = position)
                        requireActivity().getShPrString(id)?.let { path -> File(path).delete() }
                        if (adapter.bookThatReads.isEmpty()) viewModel.listIsEmpty()
                    }
                DialogInterface.BUTTON_NEGATIVE -> showToast(R.string.cancle_delete)
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setMessage(R.string.default_delete_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .setOnCancelListener { showToast(R.string.cancle_delete) }
            .create()

        dialog.show()
    }
}