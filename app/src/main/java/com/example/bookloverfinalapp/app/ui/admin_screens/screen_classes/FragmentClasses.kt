package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.base.GenericAdapter
import com.example.bookloverfinalapp.app.base.ItemOnClickListener
import com.example.bookloverfinalapp.app.models.SchoolClassModel
import com.joseph.ui_core.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.extensions.show
import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.bookloverfinalapp.databinding.DialogAddClassBinding
import com.example.bookloverfinalapp.databinding.FragmentClassesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentClasses :
    BaseFragment<FragmentClassesBinding, FragmentClassesViewModel>(FragmentClassesBinding::inflate),
    ItemOnClickListener {

    override val viewModel: FragmentClassesViewModel by viewModels()

    private val adapter: GenericAdapter by lazy(LazyThreadSafetyMode.NONE) {
        GenericAdapter(actionListener = this)
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

        viewModel.classAdapterModelsCollect(viewLifecycleOwner) { classes ->
            adapter.map(classes.swapElements().toMutableList())
        }
        viewModel.classesCollect(viewLifecycleOwner) { classes ->
            allClassesList = classes.toMutableList()
        }
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
                            addClass(newSchoolClass = "${classTitleText.text.trim()}-${classTypeText.text.trim()}")
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
                schoolId = currentUser.schoolId)
//                .observe(viewLifecycleOwner) {
//                showToast(R.string.class_added_successfully)
//                allClassesList.add(SchoolClass(id = it,
//                    title = newSchoolClass,
//                    schoolId = currentUser.schoolId))
//
//                adapter.addItem(
//                    SchoolClassModel(id = it,
//                    title = newSchoolClass,
//                    schoolId = currentUser.schoolId)
//                )
//
//                classNumberCurrentIndex = 0
//                classTypeCurrentIndex = 0
//            }
        }

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


    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).show()
    }

    override fun showAnotherFragment(item: ItemUi) {
        val classUi = item as SchoolClassModel
        viewModel.goClassStudentFragment(schoolClass = SchoolClass(id = classUi.id,
            schoolId = classUi.schoolId,
            title = classUi.title))

    }

    override fun deleteItem(item: ItemUi, position: Int) {
        val questionUi = item as SchoolClassModel
        val id = questionUi.id
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteClass(id = id)
//                        .observe(viewLifecycleOwner) {
//                        showToast(R.string.class_deleted_successfully)
//                        val iterator = allClassesList.iterator()
//                        while (iterator.hasNext()) {
//                            val item = iterator.next()
//                            if (item.id == id) {
//                                iterator.remove()
//                            }
//                        }
//                    }
                    adapter.deleteItem(position = position)
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

    override fun updateItem(item: ItemUi, position: Int) = Unit
}