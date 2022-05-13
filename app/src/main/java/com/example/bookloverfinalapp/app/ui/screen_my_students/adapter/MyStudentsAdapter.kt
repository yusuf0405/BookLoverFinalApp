package com.example.bookloverfinalapp.app.ui.screen_my_students.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.bookloverfinalapp.app.models.StudentImage
import com.example.bookloverfinalapp.app.utils.extensions.hideView
import com.example.bookloverfinalapp.app.utils.extensions.makeView
import com.facebook.shimmer.ShimmerFrameLayout
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class MyStudentsAdapter(private val actionListener: MyStudentOnClickListener) :
    RecyclerView.Adapter<MyStudentsAdapter.StudentViewHolder>() {

    var students: List<StudentAdapterModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    abstract class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(student: StudentAdapterModel) {}

        class EmptyList(view: View) : MyStudentsAdapter.StudentViewHolder(view)

        class FullScreenProgress(view: View) : StudentViewHolder(view) {
            private val shimmerLayout =
                itemView.findViewById<ShimmerFrameLayout>(R.id.student_shimmer_layout)

            override fun bind(student: StudentAdapterModel) {
                shimmerLayout.startShimmer()
            }
        }

        class Fail(view: View, private val actionListener: MyStudentOnClickListener) :
            StudentViewHolder(view) {
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
                        sessionToken: String,
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
                        image: StudentImage
                    ) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }

        class Base(view: View, private val actionListener: MyStudentOnClickListener) :
            StudentViewHolder(view) {
            private val nameText = itemView.findViewById<TextView>(R.id.progressProfileName)
            private val lastNameText = itemView.findViewById<TextView>(R.id.progressProfileLastName)
            private val chaptersReadText =
                itemView.findViewById<TextView>(R.id.countOfStudentDimonds)
            private val chaptersReadImage = itemView.findViewById<ImageView>(R.id.studentsDiamond)
            private val booksReadText = itemView.findViewById<TextView>(R.id.countOfStudentCrowns)
            private val booksReadImage = itemView.findViewById<ImageView>(R.id.progressCrown)
            private val progressText = itemView.findViewById<TextView>(R.id.countOfStudentPages)
            private val progressImage = itemView.findViewById<ImageView>(R.id.studentsPages)
            private val imageView = itemView.findViewById<CircleImageView>(R.id.studentProfileImage)
            override fun bind(student: StudentAdapterModel) {
                student.map(object : StudentAdapterModel.UserStringMapper {
                    override fun map(message: String) {}

                    override fun map(
                        objectId: String,
                        sessionToken: String,
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
                        image: StudentImage,
                    ) {
                        if (progress == 0) {
                            progressText.hideView()
//                            progressImage.hideView()
                        }
                        if (chaptersRead == 0) {
                            chaptersReadText.hideView()
//                            chaptersReadImage.hideView()
                        }
                        if (booksRead == 0) {
                            booksReadText.hideView()
//                            booksReadImage.hideView()
                        }
                        lastNameText.text = lastname
                        nameText.text = name
                        chaptersReadText.text = chaptersRead.toString()
                        booksReadText.text = booksRead.toString()
                        progressText.text = progress.toString()
                        Glide.with(itemView.context)
                            .load(image.url)
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
                                image = StudentImage(name = image.name,
                                    type = image.type,
                                    url = image.url),
                                sessionToken = sessionToken
                            ))
                        }

                    }
                })

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder =
        when (viewType) {
            0 -> StudentViewHolder.Base(R.layout.item_student.makeView(parent = parent),
                actionListener)
            1 -> StudentViewHolder.Fail(R.layout.item_fail_fullscreen.makeView(parent = parent),
                actionListener = actionListener)
            2 -> StudentViewHolder.FullScreenProgress(R.layout.shimmer_my_student.makeView(
                parent = parent))
            3 -> StudentViewHolder.EmptyList(R.layout.item_empty_student_list.makeView(parent = parent))
            else -> throw ClassCastException()
        }


    override fun getItemViewType(position: Int): Int = when (students[position]) {
        is StudentAdapterModel.Base -> 0
        is StudentAdapterModel.Fail -> 1
        is StudentAdapterModel.Progress -> 2
        is StudentAdapterModel.Empty -> 3
        else -> 4
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position])
    }
}

interface MyStudentOnClickListener {

    fun tryAgain()

    fun goStudentDetails(student: Student)
}