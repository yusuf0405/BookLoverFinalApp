package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.extensions.downEffect
import com.example.bookloverfinalapp.databinding.ItemChapterBinding
import com.shockwave.pdfium.PdfDocument


class ChapterAdapter(private val actionListener: ChapterItemOnClickListener) :
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    var chapters: List<PdfDocument.Bookmark> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    var isReading = arrayOf<Boolean>()

    inner class ViewHolder(private val binding: ItemChapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val chapter = chapters[position]
            binding.apply {
                chapterText.text = chapter.title
                if (isReading[position]) {
                    when (itemView.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_NO -> chapterText.setTextColor(Color.BLUE)
                        Configuration.UI_MODE_NIGHT_YES -> chapterText.setTextColor(Color.WHITE)
                    }
                    itemView.isEnabled = true
                } else {
                    chapterText.setTextColor(Color.GRAY)
                    itemView.isEnabled = false
                }
               itemView.downEffect().setOnClickListener {
                    actionListener.goReadPdfFragment(chapter = chapter,
                        position = position)
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

interface ChapterItemOnClickListener {
    fun goReadPdfFragment(chapter: PdfDocument.Bookmark, position: Int)
}