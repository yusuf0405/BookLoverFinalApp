package com.example.bookloverfinalapp.app.ui.screen_student_details.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.screen_student_details.adapter.MyStudentBookOnClickListener
import com.example.bookloverfinalapp.app.ui.screen_student_details.adapter.MyStudentBooksAdapter
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentStudentDetails :
    BaseFragment<FragmentStudentDetailsBinding, FragmentStudentDetailsViewModel>(
        FragmentStudentDetailsBinding::inflate), MyStudentBookOnClickListener {

    override val viewModel: FragmentStudentDetailsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val student: Student by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentDetailsArgs.fromBundle(requireArguments()).student
    }

    private val adapter: MyStudentBooksAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyStudentBooksAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
        binding().toolbar.setNavigationOnClickListener { viewModel.goBack() }

        binding().deleteProfileButton.setOnClickListener {
            viewModel.deleteStudent(id = student.objectId, sessionToken = student.sessionToken)
                .observe(viewLifecycleOwner) {
                    showToast(R.string.student_deleted_successfuly)
                    viewModel.goBack()
                }
        }

    }

    private fun setupUi() {
        binding().apply {
            if (currentUser.userType == UserType.admin) deleteProfileButton.showView()
            else deleteProfileButton.hideView()
            toolbar.apply {
                title = student.fullName()
                setToolbarColor(toolbar = toolbar)
            }
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

}