package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.adaper

import android.annotation.SuppressLint
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookPdf
import com.example.bookloverfinalapp.app.models.BookPoster
import com.example.bookloverfinalapp.app.utils.cons.ID_BOOK_DELETE
import com.example.bookloverfinalapp.app.utils.cons.ID_BOOK_UPDATE
import com.example.bookloverfinalapp.app.utils.extensions.glide
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.example.bookloverfinalapp.databinding.ItemAdminBookBinding
import com.example.bookloverfinalapp.databinding.ItemFailFullscreenBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.*

class AdminBookAdapter(private val actionListener: AdminBookItemOnClickListener) :
    RecyclerView.Adapter<AdminBookAdapter.BookViewHolder>() {

    var books: MutableList<BookAdapterModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(book: BookAdapterModel) {}

        class FullScreenProgress(view: View) : BookViewHolder(view) {
            private val shimmerView =
                itemView.findViewById<ShimmerFrameLayout>(R.id.book_shimmer_effect)

            override fun bind(book: BookAdapterModel) {
                shimmerView.startShimmer()
            }
        }

        class Fail(view: View, private val actionListener: AdminBookItemOnClickListener) :
            BookViewHolder(view) {
            val binding = ItemFailFullscreenBinding.bind(view)
            override fun bind(book: BookAdapterModel) {
                binding.tryAgain.setOnClickListener { actionListener.tryAgain() }

                book.map(object : BookAdapterModel.StringMapper {
                    override fun map(text: String) {
                        binding.messageTextView.text = text
                    }

                    override fun map(
                        genres: List<String>,
                        author: String,
                        createdAt: Date,
                        id: String,
                        page: Int,
                        publicYear: String,
                        book: BookPdf,
                        title: String,
                        chapterCount: Int,
                        poster: BookPoster,
                        updatedAt: Date
                    ) {}
                })

            }
        }

        class Base(view: View, private val actionListener: AdminBookItemOnClickListener) :
            BookViewHolder(view) {
            val binding = ItemAdminBookBinding.bind(view)
            override fun bind(book: BookAdapterModel) {
                book.map(object : BookAdapterModel.StringMapper {
                    override fun map(text: String) {}

                    override fun map(
                        genres: List<String>,
                        author: String,
                        createdAt: Date,
                        id: String,
                        page: Int,
                        publicYear: String,
                        book: BookPdf,
                        title: String,
                        chapterCount: Int,
                        poster: BookPoster,
                        updatedAt: Date,
                    ) {
                        binding.apply {
                            moreButton.tag = Book(
                                title = title,
                                author = author,
                                updatedAt = updatedAt,
                                page = page,
                                createdAt = createdAt,
                                chapterCount = chapterCount,
                                publicYear = publicYear,
                                poster = BookPoster(name = poster.name, url = poster.url),
                                book = BookPdf(name = book.name, url = book.url),
                                objectId = id,
                                genres = genres
                            )
                            bookTitle.text = title
                            bookAuthor.text = author
                            publishedYear.text = publicYear
                            bookPages.text = page.toString()
                            itemView.context.glide(poster.url, roundedBookImage)
                        }
                        binding.moreButton.setOnClickListener {
                            showPopupMenu(binding.moreButton, position)
                        }

                        itemView.setOnClickListener {
                            actionListener.goChapterBookFragment(book = binding.moreButton.tag as Book)
                        }

                    }
                })

            }

            fun showPopupMenu(view: View, position: Int) {
                val popupMenu = PopupMenu(view.context, view)
                val context = view.context
                val book = view.tag as Book
                popupMenu.menu.add(0, ID_BOOK_DELETE, Menu.NONE, context.getString(R.string.delete))
                popupMenu.menu.add(1,
                    ID_BOOK_UPDATE,
                    Menu.NONE,
                    context.getString(R.string.more_detailed))

                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        ID_BOOK_DELETE -> actionListener.deleteBook(id = book.objectId,
                            position = position)
                        ID_BOOK_UPDATE -> actionListener.updateBook(book = book,
                            position = position)
                    }
                    return@setOnMenuItemClickListener true
                }
                popupMenu.show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        when (viewType) {
            0 -> BookViewHolder.Base(R.layout.item_admin_book.makeView(parent = parent),
                actionListener)
            1 -> BookViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent),
                actionListener = actionListener)
            2 -> BookViewHolder.FullScreenProgress(R.layout.shimmer_book.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (books[position]) {
        is BookAdapterModel.Base -> 0
        is BookAdapterModel.Fail -> 1
        is BookAdapterModel.Progress -> 2
        else -> 3
    }

    fun deleteBook(position: Int) {
        books.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateBook(position: Int, newBook: BookAdapterModel.Base) {
        books[position] = newBook
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int = books.size


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }
}

interface AdminBookItemOnClickListener {
    fun tryAgain()

    fun goChapterBookFragment(book: Book)

    fun deleteBook(id: String, position: Int)

    fun updateBook(position: Int, book: Book)
}