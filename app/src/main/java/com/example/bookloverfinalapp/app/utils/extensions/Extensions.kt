package com.example.bookloverfinalapp.app.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.bookloverfinalapp.app.models.Chapter
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.domain.models.student.UserDomainImage
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseFile
import com.shockwave.pdfium.PdfDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner) { it?.let(observer) }
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Int.makeView(parent: ViewGroup): View =
    LayoutInflater.from(parent.context).inflate(this, parent, false)

fun Fragment.showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun PdfDocument.Bookmark.toChapter(isRead: Boolean): Chapter =
    Chapter(title = title, isRead = false, pageIdx = pageIdx)

fun EditText.text() {
    this.text.toString()
}

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

fun <X, Y> LiveData<X>.map(func: (source: X) -> Y) = Transformations.map(this, func)

fun EditText.validatePhone(): Boolean {
    val phone = this.text.toString()
    return phone.length == 9
}

fun EditText.validateEditPhone(): Boolean {
    val phone = this.text.toString()
    return phone.length == 13
}

