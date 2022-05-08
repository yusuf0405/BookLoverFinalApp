package com.example.bookloverfinalapp.app.ui.screen_my_students.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.screen_my_students.adapter.MyStudentOnClickListener
import com.example.bookloverfinalapp.app.ui.screen_my_students.adapter.MyStudentsAdapter
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentMyStudentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMyStudents :
    BaseFragment<FragmentMyStudentsBinding, FragmentMyStudentsViewModel>(FragmentMyStudentsBinding::inflate),
    MyStudentOnClickListener {

    override val viewModel: FragmentMyStudentsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val adapter: MyStudentsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyStudentsAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().studentsRecyclerView.adapter = adapter
        viewModel.fetchMyStudents(className = currentUser.className,
            schoolName = currentUser.schoolName)

        viewModel.observe(viewLifecycleOwner) { students ->
            adapter.students = students

        }
    }

    override fun tryAgain() {
        viewModel.fetchMyStudents(className = currentUser.className,
            schoolName = currentUser.schoolName)
    }

    override fun goStudentDetails(student: Student) {
        viewModel.goStudentDetailsFragment(student = student)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}