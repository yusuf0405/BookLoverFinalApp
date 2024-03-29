package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models.UserAdapterModel
import com.joseph.ui.core.adapter.ItemFingerprint
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.core.extensions.showRoundedImage
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemUserBinding
import com.joseph.ui.core.extensions.toDp
import com.example.bookloverfinalapp.R

class ClassStudentsFingerprint : ItemFingerprint<ItemUserBinding, UserAdapterModel> {

    override fun isRelativeItem(item: Item) = item is UserAdapterModel

    override fun getLayoutId() = R.layout.item_user

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemUserBinding, UserAdapterModel> {
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return ClassStudentViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<UserAdapterModel>() {

        override fun areItemsTheSame(oldItem: UserAdapterModel, newItem: UserAdapterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: UserAdapterModel,
            newItem: UserAdapterModel
        ) = oldItem == newItem
    }

}

class ClassStudentViewHolder(
    binding: ItemUserBinding,
) : BaseViewHolder<ItemUserBinding, UserAdapterModel>(binding) {

    override fun onBind(item: UserAdapterModel) {
        super.onBind(item)
        with(binding) {
            container.startSlideInLeftAnim()
            progressProfileLastName.text = item.fullName()
            countOfStudentCrowns.text = item.booksReadCount.toString()
            countOfStudentPages.text = item.pagesReadCount.toString()
            countOfStudentDimonds.text = item.chaptersReadCount.toString()
            itemView.context.showRoundedImage(
                roundedSize = 4.toDp
                ,
                item.imageUrl,
                studentProfileImage
            )
            root.setOnDownEffectClickListener {
                item.listener.userItemOnClickListener(item.id)
            }
        }
    }
}

