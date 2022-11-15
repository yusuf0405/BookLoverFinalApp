package com.example.bookloverfinalapp.app.models

import android.content.Context
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
import com.example.bookloverfinalapp.app.custom.CustomView
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_ENGLISH_KEY
import com.example.bookloverfinalapp.app.utils.cons.SETTING_LANGUAGE_KEY
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import kotlinx.coroutines.Job
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

data class BookModel(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfAdapter,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterAdapter,
    var updatedAt: Date,
    var genres: List<String>,
    var viewHolderType: Int,
) : ItemUi {
    data class BookPdfAdapter(
        var name: String,
        var url: String,
        var type: String,
    )

    data class BookPosterAdapter(
        var name: String,
        var url: String,
    )

    override fun show(vararg views: CustomView, context: Context, position: Int) {
        views[VIEW_POSITION_ZERO].loadImage(poster.url)
        if (viewHolderType == ADAPTER_ADMIN_BOOK_VIEW_HOLDER) {
            views[VIEW_POSITION_FIFTH].visibility(status = true)
        }
        if (viewHolderType == ADAPTER_BOOK_VIEW_HOLDER || viewHolderType == ADAPTER_ADMIN_BOOK_VIEW_HOLDER) {
            views[VIEW_POSITION_FIRST].show(author)
            views[VIEW_POSITION_SECOND].show(publicYear)
            views[VIEW_POSITION_THIRD].show(page.toString())
            views[VIEW_POSITION_FOURTH].show(title)
        }

    }

    override fun type(): Int = viewHolderType

    override fun id(): String = id

}

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
) : ItemUi {

    private fun getCreatedAt(context: Context): String {
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
        views[VIEW_POSITION_SEVENTH].show(getCreatedAt(context))
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
            val currentUserId = SharedPreferences().getCurrentUser(context).id
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


object BookLoadingModel : ItemUi {

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

object CubeEmptyModel : ItemUi {

    override fun type(): Int = ADAPTER_CUBE_EMPTY_VIEW_HOLDER

    override fun id(): String = UUID.randomUUID().toString()

    override fun show(vararg views: CustomView, context: Context, position: Int) = Unit
}