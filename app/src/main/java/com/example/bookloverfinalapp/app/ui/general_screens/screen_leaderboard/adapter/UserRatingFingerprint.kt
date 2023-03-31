package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.UserRatingAdapterModel
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemStudentRatingBinding

class UserRatingFingerprint : ItemFingerprint<ItemStudentRatingBinding, UserRatingAdapterModel> {

    override fun isRelativeItem(item: Item) = item is UserRatingAdapterModel

    override fun getLayoutId() = R.layout.item_student_rating

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemStudentRatingBinding, UserRatingAdapterModel> {
        val binding = ItemStudentRatingBinding.inflate(layoutInflater, parent, false)
        return UserRatingViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<UserRatingAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: UserRatingAdapterModel,
            newItem: UserRatingAdapterModel
        ) = oldItem.id() == newItem.id()

        override fun areContentsTheSame(
            oldItem: UserRatingAdapterModel,
            newItem: UserRatingAdapterModel
        ) = oldItem == newItem
    }

}

class UserRatingViewHolder(
    binding: ItemStudentRatingBinding,
) : BaseViewHolder<ItemStudentRatingBinding, UserRatingAdapterModel>(binding) {

    override fun onBind(item: UserRatingAdapterModel) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        val context = classNameText.context
        classNameText.text = item.className
        val fullName = "${item.lastName} ${item.name}"
        progressProfileLastName.text = fullName
        progressProfileName.text = item.name
        val progress = "${item.progress} ${context.getString(R.string.points)}"
        countOfStudentPages.text = progress
        studentRatingPosition.text = item.userRatingPosition.toString()
        context.showImage(item.imageUrl, studentProfileImage)
    }
}
