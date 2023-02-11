package com.example.bookloverfinalapp.app.models

import android.os.Parcelable
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.appendClickable
import com.example.bookloverfinalapp.app.utils.extensions.getAttrColor
import com.example.bookloverfinalapp.app.utils.extensions.getTextUpToNLine
import com.example.bookloverfinalapp.app.utils.extensions.removeLastNChars
import com.joseph.ui_core.custom.FadingTextView
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Book(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    val description: String,
    var genreIds: List<String>,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var poster: BookPoster,
    var updatedAt: Date,
    var bookPdf: BookPdf,
    val savedStatus: SavedStatus
) : Parcelable {

    companion object {
        private const val DESCRIPTION_MAX_LINE_CONT = 9
        private const val FADE_LENGTH = 0.4F
        private const val NO_FADE_LENGTH = 0F

        fun unknown(): Book = Book(
            author = String(),
            createdAt = Date(),
            description = String(),
            id = String(),
            page = 0,
            genreIds = emptyList(),
            publicYear = String(),
            title = String(),
            chapterCount = 0,
            poster = BookPoster(String(), String()),
            updatedAt = Date(),
            bookPdf = BookPdf(String(), String()),
            savedStatus = SavedStatus.SAVED
        )

    }

    fun fullName(): String = "title = $title updatedAt = $updatedAt"


    /**
     * [DESCRIPTION_MAX_LINE_CONT] - это максималное количесиво строк для описание.
     */

    /**
     * Функция показывает описание [FadingTextView].
     * Если в описании больше [DESCRIPTION_MAX_LINE_CONT] строк, появится кнопка.
     * Когда пользователь нажимает на эту кнопку, он вызывает функцию [openFullDescription],
     * которая показывает весь текст.
     */
    fun showDescription(
        textView: FadingTextView,
        openFullDescription: () -> Unit,
        rootView: View? = null,
    ) = with(textView) {
        if (rootView != null) rootView.isVisible = description.isNotEmpty()
        if (description.isEmpty()) return@with
        movementMethod = LinkMovementMethod.getInstance()
        text = description
        post {
            if (textView.lineCount < DESCRIPTION_MAX_LINE_CONT) return@post
            showShortDescription(textView, openFullDescription)
        }
    }

    /**
     * Функция задает длину затухания для приходящее в качестве аргумента [FadingTextView].
     */
    private fun applyDescriptionFadeVisibility(textView: FadingTextView) {
        textView.setFadeLength(FADE_LENGTH)
    }

    /**
     * Функция устанавливает текст [FadingTextView] в качестве краткого описания.
     * Затем вызывает функцию, которая отоброжает полное описание [openFullDescription],
     * которое передается в эту функцию в качестве аргумента.
     * Эта функция принимает два аргумента, один для краткого описания, а другой для полного описания.
     * Первый аргумент устанавливается равным тому, что возвращается при вызове метода [fetchShortDescription].
     */
    private fun showShortDescription(
        textView: FadingTextView,
        openFullDescription: () -> Unit,
    ) {
        textView.text = buildDescription(
            shortDescription = fetchShortDescription(textView),
            fullDescription = description,
            textView = textView,
            openFullDescription = openFullDescription
        )
        applyDescriptionFadeVisibility(textView)
    }

    /**
     * Функция извлекает краткое описание до [DESCRIPTION_MAX_LINE_CONT] линии,
     * а затем удаляет из него последние N символов.
     */
    private fun fetchShortDescription(description: FadingTextView) = description
        .getTextUpToNLine(DESCRIPTION_MAX_LINE_CONT)
        .removeLastNChars((DESCRIPTION_MAX_LINE_CONT + 3))

    /**
     * Функция создает [SpannableStringBuilder] и добавляет к нему краткое описание.
     * Затем мы добавляем интерактивную область, при нажатии на которую откроется полное описание.
     * Далее, мы добавляем этот расширяемый конструктор строк к нашему [FadingTextView].
     */
    private fun buildDescription(
        shortDescription: String,
        fullDescription: String,
        textView: FadingTextView,
        openFullDescription: () -> Unit
    ) = buildSpannedString {
        val context = textView.context
        append(shortDescription)
        val color = context.getAttrColor(R.attr.blackOrWhiteColor)
        appendClickable(context.getString(R.string.read_more_tag), color) {
            showFullDescription(fullDescription, textView)
            openFullDescription()
        }
    }

    /**
     * Функция устанавливает текст [FadingTextView] в качестве полного описания.
     * Также добовляет настройки чтобы можно было отаброжать текст внутри описание как url ссылка.
     */
    private fun showFullDescription(
        playlistDescription: String,
        textView: FadingTextView,
    ) {
        textView.text = playlistDescription.trim()
        Linkify.addLinks(textView, Linkify.WEB_URLS)
        textView.setFadeLength(NO_FADE_LENGTH)
    }
}

enum class SavedStatus {
    SAVED,
    NOT_SAVED,
    SAVING
}

@Parcelize
data class BookPdf(
    var name: String,
    var url: String,
) : Parcelable {

    companion object {
        fun unknown() = BookPdf(String(), String())
    }
}

@Parcelize
data class BookPoster(
    var name: String,
    var url: String,
) : Parcelable {
    companion object {
        fun unknown() = BookPoster(String(), String())
    }
}

