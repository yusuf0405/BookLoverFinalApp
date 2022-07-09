package com.example.bookloverfinalapp.app.ui.adapter

import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.custom.MyTextView
import com.example.bookloverfinalapp.app.models.BookThatReadModel
import com.example.bookloverfinalapp.app.models.UserModel
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.*

class BookViewHolder(private val view: View, private val actionListener: ItemOnClickListener) :
    GenericViewHolder(view) {

    companion object {
        const val ID_BOOK_DELETE = 0
        const val ID_BOOK_UPDATE = 1
    }

    override fun bind(item: ItemUi) {
        ItemBookBinding.bind(view).apply {
            item.show(roundedBookImage,
                bookAuthor,
                publishedYear,
                bookPages,
                bookTitle,
                moreButton,
                context = itemView.context,
                position = adapterPosition)

            moreButton.setOnClickListener {
                moreButton.tag = item
                showPopupMenu(moreButton, adapterPosition)
            }
            itemView.downEffect().setOnClickListener {
                actionListener.showAnotherFragment(item)
            }
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val item = view.tag as ItemUi

        popupMenu.menu.add(ID_BOOK_DELETE,
            ID_BOOK_DELETE,
            Menu.NONE,
            context.getString(R.string.delete))
        popupMenu.menu.add(ID_BOOK_UPDATE,
            ID_BOOK_UPDATE,
            Menu.NONE,
            context.getString(R.string.more_detailed))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_BOOK_DELETE -> actionListener.deleteItem(item = item, position = position)
                ID_BOOK_UPDATE -> actionListener.updateItem(item = item, position = position)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}

class SimilarBookViewHolder(
    view: View,
    private val actionListener: ItemOnClickListener,
) : GenericViewHolder(view) {
    val binding = ItemSimilarBookBinding.bind(view)
    override fun bind(item: ItemUi) {
        binding.apply {
            item.show(similarBookPoster, context = itemView.context, position = adapterPosition)
            itemView.downEffect().setOnClickListener {
                actionListener.showAnotherFragment(item = item)
            }
        }
    }
}

class ErrorViewHolder(private val view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) {
        ItemFailFullscreenBinding.bind(view).apply {
            item.show(messageTextView,
                tryAgain,
                context = itemView.context,
                position = adapterPosition)
        }
    }
}

class BookThatReadViewHolder(
    private val view: View,
    private val actionListener: ItemOnClickListener,
) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) {
        ItemMyBookBinding.bind(view).apply {
            item.show(bookTitle,
                bookAuthor,
                publishedYear,
                bookPages,
                roundedBookImage,
                countOfDimonds,
                countOfPages,
                bookCratedAtText,
                context = itemView.context,
                position = adapterPosition)
            val successModel = item as BookThatReadModel
            successModel.apply {
                bookProgress.setTotal(page - 1)
                bookProgress.setProgress(progress)

                itemView.downEffect().setOnLongClickListener {
                    actionListener.deleteItem(item = item, position = adapterPosition)
                    false
                }
                itemView.setOnClickListener {
                    actionListener.showAnotherFragment(item = item)
                }

            }
        }
    }
}

class UserRatingModelViewHolder(
    private val view: View,
    private val actionListener: ItemOnClickListener,
) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) {
        ItemStudentRatingBinding.bind(view).apply {

            val user = item as UserModel
            val context = itemView.context
            val ratingPosition = adapterPosition + 1

            item.show(youText,
                studentRatingPosition,
                progressProfileLastName,
                progressProfileName,
                classNameText,
                countOfStudentPages,
                context = context,
                position = adapterPosition)

            val gold = context.getGoldColor()
            val silver = context.getSilverColor()
            val bronze = context.getBronzeColor()
            val grey = context.getGreyColor()

            context.glide(uri = user.image.url, studentProfileImage)
            when (ratingPosition) {
                VIEW_POSITION_FIRST -> {
                    studentRatingPosition.setTextColor(gold)
                    youText.setTextColor(gold)
                }
                VIEW_POSITION_SECOND -> {
                    studentRatingPosition.setTextColor(silver)
                    youText.setTextColor(silver)
                }
                VIEW_POSITION_THIRD -> {
                    studentRatingPosition.setTextColor(bronze)
                    youText.setTextColor(bronze)
                }
                else -> {
                    studentRatingPosition.setTextColor( grey)
                    youText.setTextColor(grey)
                }
            }
            itemView.downEffect().setOnClickListener {
                actionListener.showAnotherFragment(item = item)
            }
        }
    }
}

class UserModelViewHolder(
    private val view: View,
    private val actionListener: ItemOnClickListener,
) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) {
        ItemUserBinding.bind(view).apply {
            val context = itemView.context
            val user = item as UserModel
            item.show(countOfStudentPages,
                countOfStudentDimonds,
                countOfStudentCrowns,
                progressProfileLastName,
                progressProfileName,
                studentsPages,
                studentsDiamond,
                progressCrown,
                context = context,
                position = adapterPosition)
            context.glide(uri = user.image.url, studentProfileImage)

        }
        itemView.downEffect().setOnClickListener {
            actionListener.showAnotherFragment(item = item)
        }

    }
}


class BookLoadingViewHolder(view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class QuestionLoadingViewHolder(view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class BookThatReadLoadingViewHolder(view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}


class SimilarBookLoadingViewHolder(view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class CatEmptyViewHolder(val view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class UserLoadingViewHolder(view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class UserTypeModelViewHolder(private val view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) {
        item.show(view.findViewById<MyTextView>(R.id.user_type_text),
            context = itemView.context,
            position = adapterPosition)
    }
}

class CubeEmptyViewHolder(val view: View) : GenericViewHolder(view) {
    override fun bind(item: ItemUi) = Unit
}

class QuestionModelViewHolder(view: View, private val actionListener: ItemOnClickListener) :
    GenericViewHolder(view) {
    private val binding = ItemQuestionBinding.bind(view)
    override fun bind(item: ItemUi) {
        binding.apply {
            item.show(chapterText, context = itemView.context, position = adapterPosition)

            deleteQuestion.setOnClickListener {
                actionListener.deleteItem(item = item, position = adapterPosition)
            }
            itemView.downEffect().setOnClickListener {
                actionListener.showAnotherFragment(item = item)
            }
        }
    }
}

class SchoolClassModelViewHolder(view: View, private val actionListener: ItemOnClickListener) :
    GenericViewHolder(view) {
    private val binding = ItemQuestionBinding.bind(view)
    override fun bind(item: ItemUi) {
        binding.apply {
            item.show(chapterText, context = itemView.context, position = adapterPosition)

            deleteQuestion.setOnClickListener {
                actionListener.deleteItem(item = item, position = adapterPosition)
            }
            itemView.downEffect().setOnClickListener {
                actionListener.showAnotherFragment(item = item)
            }
        }
    }
}

class ViewHolderChain {

    companion object {
        const val ADAPTER_BOOK_VIEW_HOLDER = 0
        const val ADAPTER_ADMIN_BOOK_VIEW_HOLDER = 1
        const val ADAPTER_SIMILAR_BOOK_VIEW_HOLDER = 2
        const val ADAPTER_BOOK_THAT_READ_VIEW_HOLDER = 3
        const val ADAPTER_USER_RATING_VIEW_HOLDER = 4
        const val ADAPTER_USER_VIEW_HOLDER = 5
        const val ADAPTER_QUESTION_VIEW_HOLDER = 6
        const val ADAPTER_SCHOOL_CLASS_VIEW_HOLDER = 7
        const val ADAPTER_USER_TYPE_VIEW_HOLDER = 8
        const val ADAPTER_ERROR_VIEW_HOLDER = 9
        const val ADAPTER_CAT_EMPTY_VIEW_HOLDER = 10
        const val ADAPTER_CUBE_EMPTY_VIEW_HOLDER = 11
        const val ADAPTER_BOOK_LOADING_VIEW_HOLDER = 12
        const val ADAPTER_USER_LOADING_VIEW_HOLDER = 13
        const val ADAPTER_QUESTION_LOADING_VIEW_HOLDER = 14
        const val ADAPTER_SIMILAR_BOOK_LOADING_VIEW_HOLDER = 15
        const val ADAPTER_BOOK_THAT_READ_LOADING_VIEW_HOLDER = 16
    }

    fun execute(
        viewType: Int,
        parent: ViewGroup,
        actionListener: ItemOnClickListener,
    ): GenericViewHolder = when (viewType) {

        ADAPTER_BOOK_VIEW_HOLDER -> BookViewHolder(R.layout.item_book.makeView(parent = parent),
            actionListener = actionListener)

        ADAPTER_ADMIN_BOOK_VIEW_HOLDER -> BookViewHolder(R.layout.item_book.makeView(
            parent = parent),
            actionListener = actionListener)

        ADAPTER_SIMILAR_BOOK_VIEW_HOLDER -> SimilarBookViewHolder(R.layout.item_similar_book.makeView(
            parent = parent),
            actionListener = actionListener)

        ADAPTER_BOOK_THAT_READ_VIEW_HOLDER -> BookThatReadViewHolder(R.layout.item_my_book.makeView(
            parent = parent), actionListener = actionListener)

        ADAPTER_USER_RATING_VIEW_HOLDER -> UserRatingModelViewHolder(R.layout.item_student_rating.makeView(
            parent = parent), actionListener = actionListener)

        ADAPTER_USER_VIEW_HOLDER -> UserModelViewHolder(R.layout.item_user.makeView(
            parent = parent), actionListener = actionListener)

        ADAPTER_QUESTION_VIEW_HOLDER -> QuestionModelViewHolder(R.layout.item_question.makeView(
            parent = parent), actionListener = actionListener)

        ADAPTER_SCHOOL_CLASS_VIEW_HOLDER -> SchoolClassModelViewHolder(R.layout.item_question.makeView(
            parent = parent), actionListener = actionListener)

        ADAPTER_USER_TYPE_VIEW_HOLDER -> UserTypeModelViewHolder(R.layout.item_user_type.makeView(
            parent = parent))

        ADAPTER_ERROR_VIEW_HOLDER -> ErrorViewHolder(R.layout.item_fail_fullscreen.makeView(
            parent = parent))

        ADAPTER_USER_LOADING_VIEW_HOLDER -> UserLoadingViewHolder(R.layout.shimmer_my_student.makeView(
            parent = parent))

        ADAPTER_QUESTION_LOADING_VIEW_HOLDER -> QuestionLoadingViewHolder(R.layout.shimmer_class.makeView(
            parent = parent))

        ADAPTER_SIMILAR_BOOK_LOADING_VIEW_HOLDER -> SimilarBookLoadingViewHolder(R.layout.shimmer_similar_book.makeView(
            parent = parent))

        ADAPTER_BOOK_LOADING_VIEW_HOLDER -> BookLoadingViewHolder(R.layout.shimmer_book.makeView(
            parent = parent))

        ADAPTER_BOOK_THAT_READ_LOADING_VIEW_HOLDER -> BookThatReadLoadingViewHolder(R.layout.shimmer_my_book.makeView(
            parent = parent))

        ADAPTER_CUBE_EMPTY_VIEW_HOLDER -> CubeEmptyViewHolder(R.layout.item_empty_similar_books.makeView(
            parent = parent))

        else -> CatEmptyViewHolder(R.layout.item_empty_list.makeView(parent = parent))

    }
}
    
