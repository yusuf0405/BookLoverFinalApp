
package com.example.bookloverfinalapp.app.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.App
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.databinding.DialogQuestionInputBinding
import com.example.domain.models.BookPosterDomain
import com.example.domain.models.UserDomainImage
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.ByteStreams
import com.parse.ParseFile
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.*
import java.util.*

fun Fragment.setCardViewColor(cardView: MaterialCardView) {
    when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> cardView.setBackgroundColor(requireContext().getAppThemeColor())
        Configuration.UI_MODE_NIGHT_YES -> cardView.setBackgroundColor(requireContext().getGreyColor())
    }
}

fun Fragment.setToolbarColor(toolbar: Toolbar) {
    when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> {
            toolbar.apply {
                setBackgroundColor(requireContext().getAppThemeColor())
                setTitleTextColor(requireContext().getWhiteColor())
                setNavigationIcon(R.drawable.ic_baseline_white_back_24)
            }

        }
        Configuration.UI_MODE_NIGHT_YES -> {
            toolbar.apply {
                setBackgroundColor(requireContext().getGreyColor())
                setTitleTextColor(Color.WHITE)
                setNavigationIcon(R.drawable.ic_baseline_white_back_24)
            }
        }
    }
}

fun Fragment.setTabLayoutColor(tabLayout: TabLayout) {
    when (requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> {
            tabLayout.setBackgroundColor(requireContext().getAppThemeColor())
            tabLayout.setTabTextColors(
                requireContext().getColor(R.color.white),
                requireContext().getColor(R.color.white)
            )
        }
        Configuration.UI_MODE_NIGHT_YES -> tabLayout.setBackgroundColor(requireContext().getGreyColor())
    }
}


fun Fragment.image(uri: Uri): ByteArray {
    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

suspend fun InputStream.saveToFile(context: Context): String {
    val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val file = File.createTempFile("my_file", ".pdf", path)
    val result = withContext(Dispatchers.Default) {
        val save = async {
            return@async use { input -> file.outputStream().use { output -> input.copyTo(output) } }
        }
        save.await()
        return@withContext file.path
    }
    return result
}

fun Fragment.downEffect(view: View): View {
    PushDownAnim.setPushDownAnimTo(view)
    return view
}

fun Fragment.createNewPath(inputStream: InputStream, key: String) = App.applicationScope.launch {
    val newPath = inputStream.saveToFile(context = requireContext())
    requireContext().getSharedPreferences(
        key,
        Context.MODE_PRIVATE
    )
        .edit()
        .putString(key, newPath)
        .apply()
}

fun Context.showErrorDialog(message: String, listener: DialogInterface.OnClickListener) {

    val dialog = AlertDialog.Builder(this)
        .setTitle(R.string.splash_error_title)
        .setCancelable(false)
        .setMessage(message)
        .setPositiveButton(R.string.try_again, listener)
        .create()

    dialog.show()
}

fun Activity.getShPrString(key: String): String? =
    this.getSharedPreferences(key, Context.MODE_PRIVATE).getString(key, null)

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showToast(@StringRes messageRes: Int) {
    Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
}

fun Int.makeView(parent: ViewGroup): View =
    LayoutInflater.from(parent.context).inflate(this, parent, false)

fun Fragment.showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun EditText.text() = this.text.toString()


fun TextView.text() {
    this.text.toString()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun EditText.validateEmail(): Boolean {
    val email = this.text.toString()
    return email.contains("@") && email.contains(".") && email.length > 7
}

fun UserImage.toDto(): UserDomainImage = UserDomainImage(
    type = type,
    url = url,
    name = name
)

fun UserDomainImage.toDto(): UserImage = UserImage(
    type = type,
    url = url,
    name = name
)

fun Activity.intentClearTask(activity: Activity) {
    val intent = Intent(this, activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun Fragment.intentClearTask(activity: Activity) {
    val intent = Intent(requireActivity(), activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}


fun EditText.validatePassword(): Boolean {
    val password = this.text.toString()
    return password.length >= 8
}

fun EditText.validateName(): Boolean {
    val name = this.text.toString()
    return name.length >= 2
}

fun EditText.validateLastName(): Boolean {
    val lastName = this.text.toString()
    return lastName.length >= 2
}

fun ParseFile.toImage(): UserImage =
    UserImage(name = name, type = "File", url = url)

fun ParseFile.toBookDomainImage(): BookPosterDomain =
    BookPosterDomain(name = name, url = url)


fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.viewModelScope(viewModelScope: CoroutineScope) {
    viewModelScope.launch {
        this@viewModelScope.collect()
    }
}

fun EditText.validatePhone(): Boolean {
    val phone = this.text.trim().toString()
    return phone.length == 19
}

fun AppCompatActivity.findNavControllerById(fragmentContainerId: Int): NavController {
    val contentFragment = supportFragmentManager.findFragmentById(fragmentContainerId)
    val navController =
        if (contentFragment is NavHostFragment) contentFragment.navController else null
    return navController ?: throw IllegalStateException("NavController is not initialized")
}

fun Fragment.getImage() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, RESULT_LOAD_IMAGE)
}

fun Fragment.convertImageUriToByteArray(uri: Uri): ByteArray {
    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
    return convertBitmapToByteArray(bitmap)
}

fun Fragment.convertVideoUriToByteArray(uri: Uri): ByteArray {
    val stream = requireActivity().contentResolver.openInputStream(uri)
    return ByteStreams.toByteArray(stream)
}

fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

fun Fragment.showCustomInputAlertDialog(radioButton: RadioButton) {
    val dialogBinding = DialogQuestionInputBinding.inflate(layoutInflater)
    val dialog = AlertDialog.Builder(requireContext())
        .setTitle(R.string.question_dilaog_title)
        .setView(dialogBinding.root)
        .setPositiveButton(R.string.action_confirm, null)
        .create()
    dialogBinding.questionInputEditText.setText(radioButton.text)
    dialog.setOnShowListener {
        dialogBinding.questionInputEditText.requestFocus()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val enteredText = dialogBinding.questionInputEditText.text.toString()
            if (enteredText.isBlank()) {
                dialogBinding.questionInputEditText.error = getString(R.string.empty_value)
                return@setOnClickListener
            }
            radioButton.text = enteredText
            dialog.dismiss()
        }
    }
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    dialog.show()
}


fun Fragment.getFile(documentUri: Uri): File {
    val inputStream = requireActivity().contentResolver?.openInputStream(documentUri)
    var file: File
    inputStream.use { input ->
        file = File(requireActivity().cacheDir, System.currentTimeMillis().toString() + ".pdf")
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(4 * 1024)
            var read: Int = -1
            while (input?.read(buffer).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
    }
    return file
}

fun Context.showImage(uri: Uri, imageView: ImageView) {
    Glide.with(this)
        .load(uri)
        .into(imageView)
}


fun Context.getGoldColor(): Int = this.resources.getColor(R.color.gold)


fun Context.getSilverColor(): Int = this.resources.getColor(R.color.silver)


fun Context.getBronzeColor(): Int = this.resources.getColor(R.color.bronze)


fun Context.getGreyColor(): Int = this.resources.getColor(R.color.grey)

fun Context.getWhiteColor(): Int = this.resources.getColor(R.color.cardBackground)


fun Context.getAppThemeColor(): Int = this.resources.getColor(R.color.app_theme)


