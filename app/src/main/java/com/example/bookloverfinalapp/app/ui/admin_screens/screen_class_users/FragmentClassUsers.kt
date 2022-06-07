package com.example.bookloverfinalapp.app.ui.admin_screens.screen_class_users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.ui.adapter.UserModel
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.databinding.FragmentAdminClassUsersBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClassUsers :
    BaseFragment<FragmentAdminClassUsersBinding, FragmentClassUsersViewModel>(
        FragmentAdminClassUsersBinding::inflate), ItemOnClickListener {

    override val viewModel: FragmentClassUsersViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    private val schoolClass: SchoolClass by lazy(LazyThreadSafetyMode.NONE) {
        FragmentClassUsersArgs.fromBundle(requireArguments()).schoolClass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().toolbar.apply {
            title = schoolClass.title
            setNavigationOnClickListener { viewModel.goBack() }
            setToolbarColor(toolbar = binding().toolbar)
        }

        binding().studentsRecyclerView.adapter = adapter

        viewModel.fetchUsers(classId = schoolClass.id)

        viewModel.collect(viewLifecycleOwner) { users -> adapter.map(users.toMutableList()) }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }

    override fun showAnotherFragment(item: ItemUi) {
        val userUi = item as UserModel
        viewModel.goUserDetailsFragment(student = viewModel.adapterMapper.map(userUi))
    }
}