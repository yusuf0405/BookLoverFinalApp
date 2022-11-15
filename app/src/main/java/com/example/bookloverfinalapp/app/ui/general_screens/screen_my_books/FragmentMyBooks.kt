package com.example.bookloverfinalapp.app.ui.general_screens.screen_my_books

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatReadModel
import com.example.bookloverfinalapp.app.base.GenericAdapter
import com.example.bookloverfinalapp.app.base.ItemOnClickListener
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.extensions.createNewPath
import com.example.bookloverfinalapp.app.utils.extensions.getShPrString
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentMyBooksBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FragmentMyBooks :
    BaseFragment<FragmentMyBooksBinding, MyBooksViewModel>(FragmentMyBooksBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener, ItemOnClickListener {

    override val viewModel: MyBooksViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding().apply {
            myBooksRecyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener(this@FragmentMyBooks)
        }
        observeResource()
    }

    private fun observeResource() {

        viewModel.fetchMyBook(currentUser.id)

        viewModel.bookThatReadAdapterModelCollect(viewLifecycleOwner) { books ->
            adapter.map(books.swapElements().toMutableList())
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

    override fun onRefresh() {
        viewModel.onRefresh(id = currentUser.id)
        binding().swipeRefresh.apply {
            isRefreshing = true
            postDelayed({
                isRefreshing = false
            }, 1500)
        }
    }

    override fun showAnotherFragment(item: ItemUi) {
        val book = item as BookThatReadModel
        val path = requireActivity().getShPrString(book.objectId)
        if (path == null) showToast(R.string.book_is_not_ready)
        else findNavController().navigate(FragmentRootStudentBookDirections
            .actionFragmentRootBookToFragmentChapterBook(book = viewModel.adapterMapper.map(book), path = path))
    }

    override fun deleteItem(item: ItemUi, position: Int) {
        val book = item as BookThatReadModel
        val id = book.objectId
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> viewModel.deleteBook(id = id)
                    .observe(viewLifecycleOwner) {
                        adapter.deleteItem(position = position)
                        requireActivity().getShPrString(id)?.let { path -> File(path).delete() }
                        if (adapter.checkForEmptiness()) viewModel.listIsEmpty()
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

    override fun updateItem(item: ItemUi, position: Int) = Unit
}