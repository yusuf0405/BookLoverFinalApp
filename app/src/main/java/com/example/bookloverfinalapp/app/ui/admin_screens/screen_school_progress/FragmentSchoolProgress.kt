package com.example.bookloverfinalapp.app.ui.admin_screens.screen_school_progress

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.launchWhenStarted
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.FragmentSchoolProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentSchoolProgress :
    BaseFragment<FragmentSchoolProgressBinding, FragmentSchoolProgressViewModel>(
        FragmentSchoolProgressBinding::inflate) {

    override val viewModel: FragmentSchoolProgressViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private var classList = mutableListOf<SchoolClass>()
    private val classesTitleList = mutableListOf<String>()
    private var classCurrentIndex = 0
    private var classTitle = ""


    private var studentList = mutableListOf<Student>()
    private val studentNameList = mutableListOf<String>()
    private var studentCurrentIndex = 0
    private var studentName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding().schoolNameText.text = currentUser.schoolName

        binding().classEditButton.setOnClickListener {
            showClassSingleChoiceWithConfirmationAlertDialog(classesTitleList)
        }

        binding().studentEditButton.setOnClickListener {
            showStudentSingleChoiceWithConfirmationAlertDialog(studentNameList)
        }

        viewModel.fetchAllBooks(schoolId = currentUser.schoolId)

        viewModel.booksObserve(viewLifecycleOwner) { books ->
            var readPages = 0
            var readChapters = 0
            var readBooks = 0
            books.forEach { book ->
                readChapters += book.chaptersRead
                readPages += book.progress
                if (book.isReadingPages[book.isReadingPages.lastIndex]) readBooks += 1
            }
            binding().apply {
                progressClassDiamondReadText.text = readChapters.toString()
                progressClassBookReadText.text = readBooks.toString()
                progressClassPageReadText.text = readPages.toString()
                studentStatisticsContainer.showView()
            }
        }

        viewModel.classesObserve(viewLifecycleOwner) { classes ->
            classList.clear()
            classesTitleList.clear()
            classList = classes.toMutableList()
            classCurrentIndex = 0
            classList.forEach { classesTitleList.add(it.title) }
            classTitle = classesTitleList[classCurrentIndex]
            viewModel.fetchMyStudents(classId = classList[classCurrentIndex].id)
            binding().classTitleText.text = classTitle
        }

        viewModel.studentObserve(viewLifecycleOwner) { students ->
            if (students.isNotEmpty()) {
                studentList.clear()
                studentNameList.clear()
                studentList = students.toMutableList()
                studentCurrentIndex = 0
                studentList.forEach { studentNameList.add(it.name + " " + it.lastname) }
                studentName = studentNameList[studentCurrentIndex]
                viewModel.fetchMyBook(id = studentList[studentCurrentIndex].objectId)
                binding().studentNameText.text = studentName
            } else {
                binding().apply {
                    studentNameText.text = ""
                    studentNameList.clear()
                    studentStatisticsContainer.hideView()
                }
            }

        }

        viewModel.schoolProgress.onEach { schoolProgress ->
            binding().apply {
                progressStudentsText.text = schoolProgress.allStudents
                progressAllBookText.text = schoolProgress.allBooks
                progressDiamondReadText.text = schoolProgress.chaptersRead
                progressBookText.text = schoolProgress.booksRead
                progressPageReadText.text = schoolProgress.pageRead
            }
            loadingDialog.dismiss()
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun showClassSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.class_setup)
            .setSingleChoiceItems(list.toTypedArray(), classCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                if (index != classCurrentIndex) viewModel.fetchMyStudents(classList[index].id)
                classCurrentIndex = index
                if (classesTitleList.isNotEmpty()) {
                    classTitle = classesTitleList[index]
                    binding().classTitleText.text = classTitle
                }
            }
            .create()
        dialog.show()
    }

    private fun showStudentSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.student_setup)
            .setSingleChoiceItems(list.toTypedArray(), studentCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                if (index != studentCurrentIndex) viewModel.fetchMyBook(id = studentList[index].objectId)
                studentCurrentIndex = index
                if (studentNameList.isNotEmpty()) {
                    studentName = studentNameList[index]
                    binding().studentNameText.text = studentName
                }

            }
            .create()
        dialog.show()
    }


}