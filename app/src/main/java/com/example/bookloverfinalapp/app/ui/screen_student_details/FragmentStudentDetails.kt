package com.example.bookloverfinalapp.app.ui.screen_student_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.screen_my_books.adapters.StudentBookAdapter
import com.example.bookloverfinalapp.app.ui.screen_my_books.adapters.StudentBookItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.databinding.FragmentStudentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentStudentDetails :
    BaseFragment<FragmentStudentDetailsBinding, FragmentStudentDetailsViewModel>(
        FragmentStudentDetailsBinding::inflate), StudentBookItemOnClickListener {

    override val viewModel: FragmentStudentDetailsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val student: Student by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentDetailsArgs.fromBundle(requireArguments()).student
    }

    private val adapter: StudentBookAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StudentBookAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()

    }

    private fun setupUi() {
        binding().apply {
            val reading = "Книги, которые читает ${student.name}"
            progressProfileName.text = student.fullName()
            progressReadTime.text = student.getCreatedAt()
            progressPageReadText.text = student.progress.toString()
            progressBookReadText.text = student.booksRead.toString()
            progressDiamondReadText.text = student.chaptersRead.toString()
            readName.text = reading
            myBooksRecyclerView.adapter = adapter

            Glide.with(requireContext())
                .load(student.image.url)
                .into(studentProgressImg)

        }
    }

    private fun observeResource() {
        viewModel.fetchMyBook(id = student.objectId)
        viewModel.observe(viewLifecycleOwner) { books ->
            adapter.bookThatReads = books.toMutableList()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }

    override fun tryAgain() {
        viewModel.fetchMyBook(id = student.objectId)
    }

    override fun goChapterFragment(book: BookThatRead) {}

}