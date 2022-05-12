package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ClassAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ClassItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentClassesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClasses :
    BaseFragment<FragmentClassesBinding, FragmentClassesViewModel>(FragmentClassesBinding::inflate),
    ClassItemOnClickListener {

    override val viewModel: FragmentClassesViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val adapter: ClassAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ClassAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().adminClassesRecyclerView.adapter = adapter

        viewModel.fetchClasses(schoolId = currentUser.schoolId)

        viewModel.observe(viewLifecycleOwner) { classes ->
            adapter.classes = classes.toMutableList()
        }
    }

    override fun tryAgain() {
        viewModel.fetchClasses(schoolId = currentUser.schoolId)
    }

    override fun goAllStudentsFragment(schoolClass: SchoolClass) {
        viewModel.goClassStudentFragment(schoolClass = schoolClass)
    }

    override fun deleteClass(id: String, position: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteClass(id = id).observe(viewLifecycleOwner) {
                        showToast(R.string.class_deleted_successfully)
                    }
                    adapter.deleteClass(position = position)
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.login_out_title)
            .setCancelable(true)
            .setMessage(R.string.default_delete_class_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }


}