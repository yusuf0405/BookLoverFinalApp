package com.example.bookloverfinalapp.app.ui.screen_my_books.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.facebook.shimmer.ShimmerFrameLayout
import com.vaibhavlakhera.circularprogressview.CircularProgressView
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

interface StudentBookItemOnClickListener {

    fun tryAgain()

    fun goChapterFragment(book: BookThatRead)

    fun deleteBook(id: String, position: Int)
}

class StudentBookAdapter(private val actionListener: StudentBookItemOnClickListener) :
    RecyclerView.Adapter<StudentBookAdapter.BookViewHolder>() {

    var bookThatReads: MutableList<BookThatReadAdapterModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(book: BookThatReadAdapterModel) {}


        class EmptyList(view: View) : BookViewHolder(view)

        class FullScreenProgress(view: View) : BookViewHolder(view) {
            private val shimmerView =
                itemView.findViewById<ShimmerFrameLayout>(R.id.my_book_shimmer_layout)

            override fun bind(book: BookThatReadAdapterModel) {
                shimmerView.startShimmer()
            }
        }

        class Fail(view: View, private val actionListener: StudentBookItemOnClickListener) :
            BookViewHolder(view) {
            private val message = itemView.findViewById<TextView>(R.id.message_text_view)
            private val tryAgain = itemView.findViewById<Button>(R.id.try_again)
            override fun bind(book: BookThatReadAdapterModel) {
                tryAgain.setOnClickListener { actionListener.tryAgain() }

                book.map(object : BookThatReadAdapterModel.BookThatReadStringMapper {
                    override fun map(message: String) {
                        this@Fail.message.text = message
                    }

                    override fun map(
                        author: String,
                        createdAt: Date,
                        bookId: String,
                        objectId: String,
                        page: Int,
                        publicYear: String,
                        title: String,
                        chapterCount: Int,
                        chaptersRead: Int,
                        poster: BookThatReadPoster,
                        updatedAt: Date,
                        book: String,
                        progress: Int,
                        isReadingPages: List<Boolean>,
                    ) {
                    }
                })

            }
        }

        class Base(view: View, private val actionListener: StudentBookItemOnClickListener) :
            BookViewHolder(view) {
            private val bookTitle = itemView.findViewById<TextView>(R.id.bookTitle)
            private val bookAuthor = itemView.findViewById<TextView>(R.id.bookAuthor)
            private val countOfDiamonds = itemView.findViewById<TextView>(R.id.countOfDimonds)
            private val countOfPages = itemView.findViewById<TextView>(R.id.countOfPages)
            private val publishedYear = itemView.findViewById<TextView>(R.id.publishedYear)
            private val bookPages = itemView.findViewById<TextView>(R.id.bookPages)
            private val cratedAtText = itemView.findViewById<TextView>(R.id.cratedAtText)
            private val bookImage = itemView.findViewById<ImageView>(R.id.rounded_book_Image)
            private val bookProgressView =
                itemView.findViewById<CircularProgressView>(R.id.bookProgress)

            override fun bind(book: BookThatReadAdapterModel) {
                book.map(object : BookThatReadAdapterModel.BookThatReadStringMapper {
                    override fun map(message: String) {}

                    override fun map(
                        author: String,
                        createdAt: Date,
                        bookId: String,
                        objectId: String,
                        page: Int,
                        publicYear: String,
                        title: String,
                        chapterCount: Int,
                        chaptersRead: Int,
                        poster: BookThatReadPoster,
                        updatedAt: Date,
                        book: String,
                        progress: Int,
                        isReadingPages: List<Boolean>,
                    ) {
                        countOfDiamonds.text = chaptersRead.toString()
                        countOfPages.text = progress.toString()
                        bookProgressView.setTotal(page - 1)
                        bookTitle.text = title
                        bookAuthor.text = author
                        publishedYear.text = publicYear
                        val pages = page - 1
                        bookPages.text = pages.toString()
                        val prettyTime = PrettyTime(Locale("ru"))
                        val getCreatedAt = prettyTime.format(createdAt)
                        val bookCreatedAt = "Добавлено: $getCreatedAt"
                        cratedAtText.text = bookCreatedAt
                        bookProgressView.setProgress(progress)
                        Glide.with(itemView.context)
                            .load(poster.url)
                            .into(bookImage)
                        itemView.setOnLongClickListener {
                            actionListener.deleteBook(id = objectId,
                                position = position)
                            false
                        }
                        itemView.setOnClickListener {
                            actionListener.goChapterFragment(book = BookThatRead(
                                title = title,
                                author = author,
                                updatedAt = updatedAt,
                                page = page,
                                createdAt = createdAt,
                                chapterCount = chapterCount,
                                publicYear = publicYear,
                                poster = BookThatReadPoster(name = poster.name, url = poster.url),
                                book = book,
                                objectId = objectId,
                                isReadingPages = isReadingPages,
                                progress = progress,
                                chaptersRead = chaptersRead,
                                bookId = bookId
                            ))
                        }

                    }
                })
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        when (viewType) {
            0 -> BookViewHolder.Base(R.layout.item_my_book.makeView(parent = parent),
                actionListener)
            1 -> BookViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent),
                actionListener = actionListener)
            2 -> BookViewHolder.FullScreenProgress(R.layout.shimmer_my_book.makeView(
                parent = parent))
            3 -> BookViewHolder.EmptyList(R.layout.item_empty_list.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (bookThatReads[position]) {
        is BookThatReadAdapterModel.Base -> 0
        is BookThatReadAdapterModel.Fail -> 1
        is BookThatReadAdapterModel.Progress -> 2
        is BookThatReadAdapterModel.Empty -> 3
        else -> 4
    }

    fun deleteBook(position: Int) {
        bookThatReads.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = bookThatReads.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookThatReads[position])
    }
}