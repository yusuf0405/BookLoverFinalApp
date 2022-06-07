package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.databinding.ItemChapterBinding
import com.shockwave.pdfium.PdfDocument


class AdminChapterAdapter(private val actionListener: AdminChapterItemOnClickListener) :
    RecyclerView.Adapter<AdminChapterAdapter.ViewHolder>() {

    var chapters: List<PdfDocument.Bookmark> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    inner class ViewHolder(private val binding: ItemChapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val chapter = chapters[position]
            binding.apply {
                chapterText.text = chapter.title
                itemView.downEffect().setOnClickListener {
                    actionListener.goQuestionFragment(chapter = position + 1)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chapter, parent, false)
        val binding = ItemChapterBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = chapters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position = position)
    }
}

interface AdminChapterItemOnClickListener {
    fun goQuestionFragment(chapter: Int)
}