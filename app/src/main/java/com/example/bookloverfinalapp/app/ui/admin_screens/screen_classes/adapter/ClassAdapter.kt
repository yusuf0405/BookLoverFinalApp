package com.example.bookloverfinalapp.app.ui.admin_screens.screen_classes.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.ClassAdapterModel
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.ui.screen_my_books.adapters.StudentBookAdapter
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.facebook.shimmer.ShimmerFrameLayout

class ClassAdapter(private val actionListener: ClassItemOnClickListener) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    var classes: MutableList<ClassAdapterModel> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(book: ClassAdapterModel) {}

        class EmptyList(view: View) : ClassAdapter.ClassViewHolder(view)

        class FullScreenProgress(view: View) : ClassViewHolder(view) {
            private val shimmerView =
                itemView.findViewById<ShimmerFrameLayout>(R.id.book_shimmer_effect)

            override fun bind(book: ClassAdapterModel) {
                shimmerView.startShimmer()
            }
        }

        class Fail(view: View, private val actionListener: ClassItemOnClickListener) :
            ClassViewHolder(view) {
            private val message = itemView.findViewById<TextView>(R.id.message_text_view)
            private val tryAgain = itemView.findViewById<Button>(R.id.try_again)
            override fun bind(book: ClassAdapterModel) {
                tryAgain.setOnClickListener { actionListener.tryAgain() }

                book.map(object : ClassAdapterModel.StringMapper {
                    override fun map(text: String) {
                        message.text = text
                    }

                    override fun map(id: String, title: String, schoolId: String) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }

        class Base(view: View, private val actionListener: ClassItemOnClickListener) :
            ClassViewHolder(view) {
            private val titleView = itemView.findViewById<TextView>(R.id.chapterText)
            private val deleteButton = itemView.findViewById<ImageView>(R.id.deleteQuestion)
            override fun bind(book: ClassAdapterModel) {
                book.map(object : ClassAdapterModel.StringMapper {
                    override fun map(text: String) {}

                    override fun map(id: String, title: String, schoolId: String) {
                        titleView.text = title

                        deleteButton.setOnClickListener {
                            actionListener.deleteClass(id = id, position = position)
                        }
                        itemView.setOnClickListener {
                            actionListener.goAllStudentsFragment(schoolClass = SchoolClass(id = id,
                                title = title, schoolId = schoolId))
                        }
                    }
                })

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder =
        when (viewType) {
            0 -> ClassViewHolder.Base(R.layout.item_question.makeView(parent = parent),
                actionListener)
            1 -> ClassViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent),
                actionListener = actionListener)
            2 -> ClassViewHolder.FullScreenProgress(R.layout.shimmer_class.makeView(parent = parent))
            3 -> ClassViewHolder.EmptyList(R.layout.item_empty_list.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (classes[position]) {
        is ClassAdapterModel.Base -> 0
        is ClassAdapterModel.Fail -> 1
        is ClassAdapterModel.Progress -> 2
        is ClassAdapterModel.Empty -> 3
    }

    fun deleteClass(position: Int) {
        classes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addClass(schoolClass: ClassAdapterModel.Base) {
        classes.add(0, schoolClass)
        notifyItemInserted(0)
    }

    override fun getItemCount(): Int = classes.size

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classes[position])
    }
}

interface ClassItemOnClickListener {

    fun tryAgain()

    fun goAllStudentsFragment(schoolClass: SchoolClass)

    fun deleteClass(id: String, position: Int)
}