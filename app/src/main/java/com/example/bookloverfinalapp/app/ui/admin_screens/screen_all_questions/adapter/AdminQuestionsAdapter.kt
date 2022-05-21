package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.databinding.ItemQuestionBinding


class AdminQuestionsAdapter(private val actionListener: AdminChapterItemOnClickListener) :
    RecyclerView.Adapter<AdminQuestionsAdapter.ViewHolder>() {

    var questions: MutableList<BookQuestion> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(question: BookQuestion, position: Int) {
            binding.apply {
                val title = "Вопрос ${position + 1}"
                chapterText.text = title

                deleteQuestion.setOnClickListener {
                    actionListener.deleteQuestion(id = question.id, position = position)
                }
                itemView.setOnClickListener {
                    actionListener.goQuestionEditFragment(question = question)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        val binding = ItemQuestionBinding.bind(view)
        return ViewHolder(binding)
    }

    fun deleteQuestion(position: Int) {
        questions.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(question = questions[position], position = position)
    }
}


interface AdminChapterItemOnClickListener {

    fun deleteQuestion(id: String, position: Int)

    fun goQuestionEditFragment(question: BookQuestion)
}