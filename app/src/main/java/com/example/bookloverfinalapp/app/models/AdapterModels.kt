package com.example.bookloverfinalapp.app.models

import android.content.Context
import android.os.Parcelable
import androidx.preference.PreferenceManager
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_FIFTH
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_FIRST
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_FOURTH
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_SECOND
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_SEVENTH
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_SIXTH
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_THIRD
import com.example.bookloverfinalapp.app.base.GenericViewHolder.Companion.VIEW_POSITION_ZERO
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_ADMIN_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_LOADING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_THAT_READ_LOADING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_THAT_READ_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_CAT_EMPTY_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_CUBE_EMPTY_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_ERROR_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_QUESTION_LOADING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_QUESTION_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_SCHOOL_CLASS_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_SIMILAR_BOOK_LOADING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_LOADING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_RATING_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_USER_TYPE_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_ENGLISH_KEY
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_KEY
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.joseph.ui_core.custom.CustomView
import com.joseph.ui_core.custom.ItemUi
import kotlinx.coroutines.Job
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
data class BookThatReadModel(
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: BookThatReadPoster,
    var updatedAt: Date,
    var book: String,
    var progress: Int,
    var isReadingPages: List<Boolean>,
    var state: Parcelable? = null
) : ItemUi, Item {

    fun getProgressColor(context: Context) =
        when (progress.toFloat() / (page - 1).toFloat()) {
            in 0f..0.40f -> context.getColor(R.color.widget_icon_background)
            in 0.41f..0.70f -> context.getColor(R.color.books)
            in 0.70f..1f -> context.getColor(R.color.apple_normal)
            else -> context.getColor(R.color.white)
        }

    fun getCreatedAtText(context: Context): String {
        val language = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(SETTING_LANGUAGE_KEY, SETTING_LANGUAGE_ENGLISH_KEY)
        val prettyTime = PrettyTime(Locale(language ?: SETTING_LANGUAGE_ENGLISH_KEY))
        val getCreatedAt = prettyTime.format(createdAt)
        return "${context.getString(R.string.added)}: $getCreatedAt"
    }

    override fun type(): Int = ADAPTER_BOOK_THAT_READ_VIEW_HOLDER

    override fun id(): String = objectId

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        views[VIEW_POSITION_ZERO].show(title)
        views[VIEW_POSITION_FIRST].show(author)
        views[VIEW_POSITION_SECOND].show(publicYear)
        views[VIEW_POSITION_THIRD].show(page.toString())
        views[VIEW_POSITION_FOURTH].loadImage(poster.url)
        views[VIEW_POSITION_FIFTH].show(chaptersRead.toString())
        views[VIEW_POSITION_SIXTH].show(progress.toString())
        views[VIEW_POSITION_SEVENTH].show(getCreatedAtText(context))
    }

}

data class UserModel(
    var objectId: String,
    var classId: String,
    var createAt: Date,
    var schoolName: String,
    var className: String,
    var email: String,
    var gender: String,
    var lastname: String,
    var name: String,
    var number: String,
    var userType: String,
    var sessionToken: String,
    var chaptersRead: Int,
    var booksRead: Int,
    var progress: Int,
    val booksId: List<String>,
    var image: StudentImage,
    val viewHolderType: Int,
) : ItemUi {

    override fun type(): Int = viewHolderType

    override fun id(): String = objectId

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        if (viewHolderType == ADAPTER_USER_RATING_VIEW_HOLDER) {
            val ratingPosition = position + 1
            val currentUserId = SharedPreferences().getCurrentUser(context)?.id
            views[VIEW_POSITION_ZERO].visibility(currentUserId == objectId)
            views[VIEW_POSITION_FIRST].show(ratingPosition.toString())
            views[VIEW_POSITION_SECOND].show(lastname)
            views[VIEW_POSITION_THIRD].show(name)
            views[VIEW_POSITION_FOURTH].show(className)
            views[VIEW_POSITION_FIFTH].show(progress.toString())
        } else {

            if (progress == VIEW_POSITION_ZERO) {
                views[VIEW_POSITION_ZERO].visibility(status = false)
                views[VIEW_POSITION_FIFTH].visibility(status = false)
            }
            if (chaptersRead == VIEW_POSITION_ZERO) {
                views[VIEW_POSITION_FIRST].visibility(status = false)
                views[VIEW_POSITION_SIXTH].visibility(status = false)
            }
            if (booksRead == VIEW_POSITION_ZERO) {
                views[VIEW_POSITION_SECOND].visibility(status = false)
                views[VIEW_POSITION_SEVENTH].visibility(status = false)
            }
            views[VIEW_POSITION_ZERO].show(progress.toString())
            views[VIEW_POSITION_FIRST].show(chaptersRead.toString())
            views[VIEW_POSITION_SECOND].show(booksRead.toString())
            views[VIEW_POSITION_THIRD].show(lastname)
            views[VIEW_POSITION_FOURTH].show(name)
        }
    }

}

data class ErrorModel(private val message: String, private val function: () -> Job) : ItemUi {

    override fun type(): Int = ADAPTER_ERROR_VIEW_HOLDER

    override fun id(): String = message

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        views[VIEW_POSITION_ZERO].show(message)
        views[VIEW_POSITION_FIRST].handleClick(function)
    }
}

data class SchoolClassModel(
    var id: String,
    val title: String,
    val schoolId: String,
) : ItemUi {

    override fun type(): Int = ADAPTER_SCHOOL_CLASS_VIEW_HOLDER

    override fun id(): String = id

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        views[VIEW_POSITION_ZERO].show(title)
    }
}


object BookLoadingModel : ItemUi, Item {

    override fun type(): Int = ADAPTER_BOOK_LOADING_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit

}


object CatEmptyModel : ItemUi {

    override fun type(): Int = ADAPTER_CAT_EMPTY_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit
}

data class QuestionModel(
    val id: String,
    val question: String,
    val a: String,
    val b: String,
    val d: String,
    val c: String,
    val rightAnswer: String,
    val bookId: String,
    val chapter: String,
) : ItemUi {

    override fun type(): Int = ADAPTER_QUESTION_VIEW_HOLDER

    override fun id(): String = id

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        val question = "${context.getString(R.string.question)} ${position + 1}"
        views[VIEW_POSITION_ZERO].show(question)
    }
}

data class UserTypeModel(val userType: UserType) : ItemUi {
    override fun type(): Int = ADAPTER_USER_TYPE_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        val type = if (userType == UserType.teacher)
            context.getString(R.string.teachers)
        else context.getString(R.string.students)
        views[VIEW_POSITION_ZERO].show(type)

    }
}

object UserLoadingModel : ItemUi {

    override fun type(): Int = ADAPTER_USER_LOADING_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit

}

object SimilarBookLoadingModel : ItemUi {

    override fun type(): Int = ADAPTER_SIMILAR_BOOK_LOADING_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit


}

object QuestionLoadingModel : ItemUi {

    override fun type(): Int = ADAPTER_QUESTION_LOADING_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit

}

object BookThatReadLoadingModel : ItemUi {

    override fun type(): Int = ADAPTER_BOOK_THAT_READ_LOADING_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit

}

object CubeEmptyModel : ItemUi, Item {

    override fun type(): Int = ADAPTER_CUBE_EMPTY_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit
}