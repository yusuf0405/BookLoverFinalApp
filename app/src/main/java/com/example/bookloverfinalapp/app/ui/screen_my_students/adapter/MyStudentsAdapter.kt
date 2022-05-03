package com.example.bookloverfinalapp.app.ui.screen_my_students.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.bookloverfinalapp.app.models.StudentImage
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.example.loadinganimation.LoadingAnimation
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class MyStudentsAdapter(private val actionListener: MyStudentOnClickListener) :
    RecyclerView.Adapter<MyStudentsAdapter.BookViewHolder>() {

    var students: List<StudentAdapterModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(student: StudentAdapterModel) {}

        class FullScreenProgress(view: View) : BookViewHolder(view) {
            private val loadingDialog = itemView.findViewById<LoadingAnimation>(R.id.loadingAnim)
            override fun bind(student: StudentAdapterModel) {
                loadingDialog.setTextMsg(itemView.context.getString(R.string.loading_books))
                loadingDialog.setTextViewVisibility(true)
                loadingDialog.setTextStyle(true)
            }
        }

        class Fail(view: View, private val actionListener: MyStudentOnClickListener) :
            BookViewHolder(view) {
            private val message = itemView.findViewById<TextView>(R.id.message_text_view)
            private val tryAgain = itemView.findViewById<Button>(R.id.try_again)
            override fun bind(student: StudentAdapterModel) {
                tryAgain.setOnClickListener { actionListener.tryAgain() }

                student.map(object : StudentAdapterModel.UserStringMapper {
                    override fun map(message: String) {
                        this@Fail.message.text = message
                    }

                    override fun map(
                        objectId: String,
                        classId: String,
                        createAt: Date,
                        schoolName: String,
                        className: String,
                        email: String,
                        gender: String,
                        lastname: String,
                        name: String,
                        number: String,
                        userType: String,
                        chaptersRead: Int,
                        booksRead: Int,
                        progress: Int,
                        booksId: List<String>,
                        image: StudentImage?,
                    ) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }

        class Base(view: View, private val actionListener: MyStudentOnClickListener) :
            BookViewHolder(view) {
            private val nameText = itemView.findViewById<TextView>(R.id.progressProfileName)
            private val lastNameText = itemView.findViewById<TextView>(R.id.progressProfileLastName)
            private val chaptersReadText =
                itemView.findViewById<TextView>(R.id.countOfStudentDimonds)
            private val booksReadText = itemView.findViewById<TextView>(R.id.countOfStudentCrowns)
            private val progressText = itemView.findViewById<TextView>(R.id.countOfStudentPages)
            private val imageView = itemView.findViewById<CircleImageView>(R.id.studentProfileImage)
            override fun bind(student: StudentAdapterModel) {
                student.map(object : StudentAdapterModel.UserStringMapper {
                    override fun map(message: String) {}

                    override fun map(
                        objectId: String,
                        classId: String,
                        createAt: Date,
                        schoolName: String,
                        className: String,
                        email: String,
                        gender: String,
                        lastname: String,
                        name: String,
                        number: String,
                        userType: String,
                        chaptersRead: Int,
                        booksRead: Int,
                        progress: Int,
                        booksId: List<String>,
                        image: StudentImage?,
                    ) {
                        lastNameText.text = lastname
                        nameText.text = name
                        chaptersReadText.text = chaptersRead.toString()
                        booksReadText.text = booksRead.toString()
                        progressText.text = progress.toString()
                        Glide.with(itemView.context)
                            .load(image?.url)
                            .placeholder(R.drawable.placeholder_avatar)
                            .into(imageView)

                        itemView.setOnClickListener {
                            actionListener.goStudentDetails(student = Student(
                                objectId = objectId,
                                classId = classId,
                                createAt = createAt,
                                schoolName = schoolName,
                                className = className,
                                email = email,
                                gender = gender,
                                lastname = lastname,
                                name = name,
                                number = number,
                                userType = userType,
                                chaptersRead = chaptersRead,
                                booksRead = booksRead,
                                progress = progress,
                                booksId = booksId,
                            ))
                        }

                    }
                })

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        when (viewType) {
            0 -> BookViewHolder.Base(R.layout.item_student.makeView(parent = parent),
                actionListener)
            1 -> BookViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent),
                actionListener = actionListener)
            2 -> BookViewHolder.FullScreenProgress(R.layout.item_progress_fullscreen.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (students[position]) {
        is StudentAdapterModel.Base -> 0
        is StudentAdapterModel.Fail -> 1
        is StudentAdapterModel.Progress -> 2
        else -> 3
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(students[position])
    }
}

interface MyStudentOnClickListener {

    fun tryAgain()

    fun goStudentDetails(student: Student)
}