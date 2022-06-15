package com.example.bookloverfinalapp.app.ui.general_screens.screen_my_students

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.ui.adapter.UserModel
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.FragmentMyStudentsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMyStudents :
    BaseFragment<FragmentMyStudentsBinding, FragmentMyStudentsViewModel>(FragmentMyStudentsBinding::inflate),
    ItemOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    override val viewModel: FragmentMyStudentsViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().apply {
            studentsRecyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener(this@FragmentMyStudents)
        }
        viewModel.fetchMyStudents(classId = currentUser.classId)

        viewModel.collect(viewLifecycleOwner) { students ->
            adapter.map(students.swapElements().toMutableList())
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }

    override fun onRefresh() {
        viewModel.onRefresh(classId = currentUser.classId)
        binding().swipeRefresh.apply {
            isRefreshing = true
            postDelayed({ isRefreshing = false }, 1500)
        }
    }

    override fun showAnotherFragment(item: ItemUi) {
        val userUi = item as UserModel
        viewModel.goStudentDetailsFragment(student = viewModel.adapterMapper.map(userUi))
    }
}