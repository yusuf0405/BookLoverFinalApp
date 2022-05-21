package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.ClassAdapterModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.adapter.ClassAdapter
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.adapter.ClassItemOnClickListener
import com.example.bookloverfinalapp.app.utils.extensions.showView
import com.example.bookloverfinalapp.databinding.DialogAddClassBinding
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

    private var allClassesList = mutableListOf<SchoolClass>()
    private var classNumbers = mutableListOf<Int>()
    private var classTypes = mutableListOf<String>()
    private var classNumberCurrentIndex = 0
    private var classTypeCurrentIndex = 0
    private var classNumber: String? = null
    private var classType: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addedClass()
        binding().adminClassesRecyclerView.adapter = adapter

        binding().fab.setOnClickListener {
            showCustomAlertDialog()
        }
        viewModel.fetchClasses(schoolId = currentUser.schoolId)

        viewModel.observe(viewLifecycleOwner) { classes ->
            adapter.classes = classes.toMutableList()
        }
        viewModel.classObserve(viewLifecycleOwner) { classes ->
            allClassesList = classes.toMutableList()
        }
    }

    override fun tryAgain() {
        viewModel.fetchClasses(schoolId = currentUser.schoolId)
    }

    override fun goAllStudentsFragment(schoolClass: SchoolClass) {
        viewModel.goClassStudentFragment(schoolClass = schoolClass)
    }

    private fun showCustomAlertDialog() {
        val dialogBinding = DialogAddClassBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_class)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.action_confirm, null)
            .setOnCancelListener {
                classTypeCurrentIndex = 0
                classTypeCurrentIndex = 0
            }
            .create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                dialogBinding.apply {
                    when {
                        classTitleText.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                        classTypeText.text.isBlank() -> showToast(R.string.fill_in_all_fields)
                        else -> {
                            addClass("${classTitleText.text.trim()}-${classTypeText.text.trim()}")
                            dialog.dismiss()
                        }
                    }
                }
            }
        }

        dialogBinding.classNumberCardView.setOnClickListener {
            showNumbersSingleChoiceWithConfirmationAlertDialog(dialogBinding.classTitleText)

        }
        dialogBinding.classTypeCardView.setOnClickListener {
            showTypesSingleChoiceWithConfirmationAlertDialog(dialogBinding.classTypeText)
        }

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    private fun addClass(newSchoolClass: String) {
        var counter = 0
        allClassesList.forEach { schoolClass ->
            if (schoolClass.title == newSchoolClass) showToast(R.string.such_a_class_already_exists)
            else counter++

        }
        if (counter == allClassesList.size) {
            viewModel.addNewClass(title = newSchoolClass,
                schoolId = currentUser.schoolId).observe(viewLifecycleOwner) {
                showToast(R.string.class_added_successfully)
                allClassesList.add(SchoolClass(id = it,
                    title = newSchoolClass,
                    schoolId = currentUser.schoolId))
                adapter.addClass(ClassAdapterModel.Base(id = it,
                    title = newSchoolClass,
                    schoolId = currentUser.schoolId))
                classNumberCurrentIndex = 0
                classTypeCurrentIndex = 0
            }
        }

    }

    override fun deleteClass(id: String, position: Int) {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteClass(id = id).observe(viewLifecycleOwner) {
                        showToast(R.string.class_deleted_successfully)
                        val iterator = allClassesList.iterator()
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            if (item.id == id) {
                                iterator.remove()
                            }
                        }
                    }
                    adapter.deleteClass(position = position)
                }
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.login_out_title)
            .setCancelable(true)
            .setMessage(R.string.default_delete_class_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()

        dialog.show()
    }


    private fun showNumbersSingleChoiceWithConfirmationAlertDialog(textView: TextView) {
        val dialog = AlertDialog.Builder(requireContext())
            .setSingleChoiceItems(classNumbers.map { it.toString() }.toTypedArray(),
                classNumberCurrentIndex,
                null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classNumberCurrentIndex = index
                classNumber = classNumbers[index].toString()
                textView.text = classNumber.toString()
            }
            .create()
        dialog.show()
    }

    private fun showTypesSingleChoiceWithConfirmationAlertDialog(textView: TextView) {
        val dialog = AlertDialog.Builder(requireContext())
            .setSingleChoiceItems(classTypes.toTypedArray(), classTypeCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classTypeCurrentIndex = index
                classType = classTypes[index]
                textView.text = classType
            }
            .create()
        dialog.show()
    }

    private fun addedClass() {
        for (i in 1 until 12) {
            classNumbers.add(i)
        }
        classTypes.add("А")
        classTypes.add("Б")
        classTypes.add("В")
        classTypes.add("Г")
        classTypes.add("Д")
        classTypes.add("Е")
        classTypes.add("Ж")
        classTypes.add("З")
        classTypes.add("И")
        classTypes.add("К")
        classTypes.add("Л")
        classTypes.add("М")
        classTypes.add("Н")
        classTypes.add("О")
        classTypes.add("П")
        classTypes.add("Р")
        classTypes.add("С")
        classTypes.add("Т")
        classTypes.add("У")
        classTypes.add("Ф")
        classTypes.add("Х")
        classTypes.add("Ч")
        classTypes.add("Ш")
        classTypes.add("Э")
        classTypes.add("Ю")
        classTypes.add("Я")
    }


    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).showView()
    }
}