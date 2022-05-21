package com.example.bookloverfinalapp.app.ui.screen_book_details.adapters

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookPdf
import com.example.bookloverfinalapp.app.models.BookPoster
import com.example.bookloverfinalapp.app.utils.extensions.glide
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.example.bookloverfinalapp.databinding.ItemFailFullscreenBinding
import com.example.bookloverfinalapp.databinding.ItemSimilarBookBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.*

class SimilarBookAdapter(private val actionListener: BookItemOnClickListener) :
    RecyclerView.Adapter<SimilarBookAdapter.BookViewHolder>() {

    var books: List<BookAdapterModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(book: BookAdapterModel) {}

        class FullScreenProgress(view: View) : BookViewHolder(view) {
            private val shimmerView = itemView.findViewById<ShimmerFrameLayout>(R.id.book_shimmer_effect)
            override fun bind(book: BookAdapterModel) { shimmerView.startShimmer() }
        }

        class Fail(view: View, private val actionListener: BookItemOnClickListener) :
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
                        updatedAt: Date,
                    ) {
                    }
                })

            }
        }

        class Base(view: View, private val actionListener: BookItemOnClickListener) :
            BookViewHolder(view) {
            val binding = ItemSimilarBookBinding.bind(view)
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

                        itemView.context.glide(poster.url, binding.similarBookPoster)

                        itemView.setOnClickListener {
                            actionListener.goChapterBookFragment(book = Book(
                                title = title,
                                author = author,
                                updatedAt = updatedAt,
                                page = page,
                                createdAt = createdAt,
                                chapterCount = chapterCount,
                                publicYear = publicYear,
                                poster = BookPoster(name = poster.name, url = poster.url),
                                book = BookPdf(name = book.name, url = book.url),
                                objectId = id, genres = genres
                            ))
                        }

                    }
                })

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        when (viewType) {
            0 -> BookViewHolder.Base(R.layout.item_similar_book.makeView(parent = parent), actionListener)
            1 -> BookViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent), actionListener = actionListener)
            2 -> BookViewHolder.FullScreenProgress(R.layout.shimmer_similar_book.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (books[position]) {
        is BookAdapterModel.Base -> 0
        is BookAdapterModel.Fail -> 1
        is BookAdapterModel.Progress -> 2
        else -> 3
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }
}

interface BookItemOnClickListener {

    fun tryAgain()

    fun goChapterBookFragment(book: Book)
}