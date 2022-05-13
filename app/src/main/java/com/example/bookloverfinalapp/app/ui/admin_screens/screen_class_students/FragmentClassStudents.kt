package com.example.bookloverfinalapp.app.ui.admin_screens.screen_class_students

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.screen_my_students.adapter.MyStudentOnClickListener
import com.example.bookloverfinalapp.app.ui.screen_my_students.adapter.MyStudentsAdapter
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.databinding.FragmentClassStudentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClassStudents :
    BaseFragment<FragmentClassStudentsBinding, FragmentClassStudentsViewModel>(
        FragmentClassStudentsBinding::inflate), MyStudentOnClickListener {

    override val viewModel: FragmentClassStudentsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val adapter: MyStudentsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyStudentsAdapter(actionListener = this)
    }

    private val schoolClass: SchoolClass by lazy(LazyThreadSafetyMode.NONE) {
        FragmentClassStudentsArgs.fromBundle(requireArguments()).schoolClass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

        viewModel.fetchMyStudents(classId = schoolClass.id)

        viewModel.observe(viewLifecycleOwner) { students ->
            adapter.students = students

        }
    }

    private fun setupUi() {
        binding().apply {
            toolbar.apply {
                title = schoolClass.title
                setTitleTextColor(Color.WHITE)
                setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                setNavigationOnClickListener { viewModel.goBack() }
            }
            studentsRecyclerView.adapter = adapter


        }
    }

    override fun tryAgain() {
        viewModel.fetchMyStudents(classId = currentUser.classId)
    }

    override fun goStudentDetails(student: Student) {
        viewModel.goStudentDetailsFragment(student = student)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }

}