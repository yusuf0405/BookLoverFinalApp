package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.adapter.GenericAdapter
import com.example.bookloverfinalapp.app.ui.adapter.ItemOnClickListener
import com.example.bookloverfinalapp.app.models.UserModel
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.DialogFilterClassRatingBinding
import com.example.bookloverfinalapp.databinding.FragmentProgressBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FragmentProgress :
    BaseFragment<FragmentProgressBinding, FragmentProgressViewModel>(
        FragmentProgressBinding::inflate
    ), ItemOnClickListener {

    override val viewModel: FragmentProgressViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
    }

    private var isFilter = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeResource()
    }

    private fun observeResource() {

        viewModel.fetchMyBook(id = currentUser.id)

        if (isFilter) viewModel.fetchSchoolStudents(schoolId = currentUser.schoolId)
        else viewModel.fetchMyStudent(classId = currentUser.classId)

        viewModel.booksThatReadCollect(viewLifecycleOwner) { books ->
            var readPages = 0
            var readChapters = 0
            var readBooks = 0
            books.forEach { book ->
                readChapters += book.chaptersRead
                readPages += book.progress
                if (book.isReadingPages[book.isReadingPages.lastIndex]) readBooks += 1
            }
            binding().apply {
                progressDiamondReadText.text = readChapters.toString()
                progressBookReadText.text = readBooks.toString()
                progressPageReadText.text = readPages.toString()
            }
        }

        viewModel.statisticsObserve(viewLifecycleOwner) { statistics ->
            binding().apply {
                statistics.apply {
                    progressClassDiamondReadText.text = readChapters.toString()
                    progressClassBookReadText.text = readBooks.toString()
                    progressClassPageReadText.text = readPages.toString()
                }
            }
        }
        viewModel.studentAdapterModelsCollect(viewLifecycleOwner) { students ->
            adapter.map(students.toMutableList())
            adapter.notifyDataSetChanged()
        }

        viewModel.collectProgressAnimation(viewLifecycleOwner) {
            binding().apply {
                it.getValue()?.let { status ->
                    if (status) {
                        if (currentUser.userType == UserType.teacher) classStatisticsContainer.hideView()
                        progressLoadingAnimation.showView()
                        classRatingContainer.hideView()
                        myStatisticsContainer.hideView()
                    } else {
                        if (currentUser.userType == UserType.teacher) classStatisticsContainer.showView()
                        progressLoadingAnimation.hideView()
                        classRatingContainer.showView()
                        myStatisticsContainer.showView()
                    }
                }
            }
        }
    }

    private fun showFilterAlertDialog() {
        val dialogBinding = DialogFilterClassRatingBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle(R.string.select_display_mode)
            .setPositiveButton(R.string.action_confirm, null)
            .create()
        dialog.setOnShowListener {
            dialogBinding.apply {
                if (isFilter) schoolFilter.isChecked = true
                else classFilter.isChecked = true
                schoolFilter.setOnClickListener {
                    isFilter = true
                    SharedPreferences().saveIsFilter(isFilter, requireContext())
                }
                classFilter.setOnClickListener {
                    isFilter = false
                    SharedPreferences().saveIsFilter(isFilter, requireContext())
                }
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    if (isFilter) viewModel.fetchSchoolStudents(schoolId = currentUser.schoolId)
                    else viewModel.fetchMyStudent(classId = currentUser.classId)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun setupUi() = binding().apply {
        isFilter = SharedPreferences().getIsFilter(requireActivity())
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        filterButton.setOnClickListener { showFilterAlertDialog() }
        studentsRatingRecyclerView.adapter = adapter
        currentUser.apply {
            val fullName = "$name $lastname"
            progressProfileName.text = fullName
            val readTime = formatter.format(createAt).toString()
            progressReadTime.text = readTime
            Glide.with(requireActivity())
                .load(image?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(studentProgressImg)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }

    override fun showAnotherFragment(item: ItemUi) {
        val studentUi = item as UserModel
        viewModel.goStudentDetailsFragment(student = viewModel.adapterMapper.map(studentUi))
    }
}