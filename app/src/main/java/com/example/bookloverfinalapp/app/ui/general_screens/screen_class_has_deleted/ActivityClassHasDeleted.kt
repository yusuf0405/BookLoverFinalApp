package com.example.bookloverfinalapp.app.ui.general_screens.screen_class_has_deleted

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.dialog.LoadingDialog
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.ActivityClassHasDeletedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityClassHasDeleted : AppCompatActivity() {

    private val binding: ActivityClassHasDeletedBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityClassHasDeletedBinding.inflate(layoutInflater)
    }
    private val currentUser: User by lazy(LazyThreadSafetyMode.NONE) {
        checkNotNull(SharedPreferences().getCurrentUser(activity = this))
    }
    private val loadingDialog: LoadingDialog by lazy(LazyThreadSafetyMode.NONE) {
        LoadingDialog(context = this, getString(R.string.loading_please_wait))
    }

    private val viewModel: ClassHasDeletedViewModel by viewModels()

    private var classList = mutableListOf<SchoolClass>()
    private val classesTitleList = mutableListOf<String>()
    private var classCurrentIndex = 0
    private var classTitle: String? = null
    private var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.fetchClasses(schoolId = currentUser.schoolId)

        binding.classEditButton.setOnClickListener {
            showClassSingleChoiceWithConfirmationAlertDialog(classesTitleList)
        }

        binding.confimButton.setOnClickListener {
            when {
                classId == null -> showToast(R.string.fill_in_all_fields)
                classTitle == null -> showToast(R.string.fill_in_all_fields)
                else ->
                    viewModel.updateStudentClass(id = currentUser.id,
                        sessionToken = currentUser.sessionToken,
                        classId = classId!!,
                        classTitle = classTitle!!)
//                        .observe(this) {
//                        val newCurrentUser = currentUser
//                        newCurrentUser.classId = classId!!
//                        newCurrentUser.className = classTitle!!
//                        SharedPreferences().saveCurrentUser(user = newCurrentUser, activity = this)
//                        intentClearTask(activity = ActivityMain())
//                    }
            }
        }
        viewModel.collectError(this) {
            it.getValue()?.let { message -> showToast(message) }
        }

        viewModel.collectProgressDialog(this) { status ->
            if (status) loadingDialog.show()
            else loadingDialog.dismiss()
        }

        viewModel.collect(this) { classes ->
            classList.clear()
            classesTitleList.clear()
            classList = classes.toMutableList()
            classCurrentIndex = 0
            classId = classList[classCurrentIndex].id
            classList.forEach { classesTitleList.add(it.title) }
            classTitle = classesTitleList[classCurrentIndex]
            binding.classTitleText.text = classTitle
        }

    }

    private fun showClassSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.class_setup)
            .setSingleChoiceItems(list.toTypedArray(), classCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classCurrentIndex = index
                classTitle = classesTitleList[index]
                if (index != 0) {
                    classId = classList[index].id
                    binding.classTitleText.text = classTitle
                }

            }
            .create()
        dialog.show()
    }


}