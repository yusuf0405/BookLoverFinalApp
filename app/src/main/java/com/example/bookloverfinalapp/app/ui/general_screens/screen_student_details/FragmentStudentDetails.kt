package com.example.bookloverfinalapp.app.ui.general_screens.screen_student_details

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.base.GenericAdapter
import com.example.bookloverfinalapp.app.base.ItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.glide
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.setToolbarColor
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentStudentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentStudentDetails :
    BaseFragment<FragmentStudentDetailsBinding, FragmentStudentDetailsViewModel>(
        FragmentStudentDetailsBinding::inflate), ItemOnClickListener {

    override val viewModel: FragmentStudentDetailsViewModel by viewModels()

    private val student: Student by lazy(LazyThreadSafetyMode.NONE) {
        FragmentStudentDetailsArgs.fromBundle(requireArguments()).student
    }

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
        binding().toolbar.setNavigationOnClickListener { viewModel.goBack() }

        binding().deleteProfileButton.setOnClickListener {
            val listener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        viewModel.deleteStudent(id = student.objectId,
                            sessionToken = student.sessionToken)
                            .observe(viewLifecycleOwner) {
                                showToast(R.string.student_deleted_successfuly)
                                viewModel.goBack()
                            }

                    DialogInterface.BUTTON_NEGATIVE -> {}
                    DialogInterface.BUTTON_NEUTRAL -> {}
                }
            }
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.login_out_title)
                .setCancelable(true)
                .setMessage(R.string.default_delete_user_alert_message)
                .setPositiveButton(R.string.action_yes, listener)
                .setNegativeButton(R.string.action_no, listener)
                .setNeutralButton(R.string.action_ignore, listener)
                .create()

            dialog.show()


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
            if (student.userType == UserType.student.name) {
                progressStatisticText.text = getString(R.string.student_statistics)
                readName.text = getString(R.string.student_books)
            } else {
                readName.text = getString(R.string.teacher_books)
                progressStatisticText.text = getString(R.string.teacher_statistics)
            }
            phoneText.text = student.number
            progressProfileName.text = student.fullName()
            progressReadTime.text = student.getCreatedAt()
            progressPageReadText.text = student.progress.toString()
            progressBookReadText.text = student.booksRead.toString()
            progressDiamondReadText.text = student.chaptersRead.toString()
            myBooksRecyclerView.adapter = adapter
            requireContext().glide(student.image.url, studentProgressImg)

        }
    }

    private fun observeResource() {

        viewModel.fetchMyBook(id = student.objectId)

        viewModel.collect(viewLifecycleOwner) { books ->
            adapter.map(books.toMutableList())
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}