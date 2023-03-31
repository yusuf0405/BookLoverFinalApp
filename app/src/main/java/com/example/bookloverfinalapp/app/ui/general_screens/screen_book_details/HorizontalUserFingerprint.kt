package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models.UserAdapterModel
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showImage
import com.example.bookloverfinalapp.databinding.ItemUserCircleBinding
import com.joseph.utils_core.extensions.toDp

class HorizontalUserFingerprint(private val isWrapContent: Boolean = false) :
    ItemFingerprint<ItemUserCircleBinding, UserAdapterModel> {

    override fun isRelativeItem(item: Item) = item is UserAdapterModel

    override fun getLayoutId() = R.layout.item_user_circle

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemUserCircleBinding, UserAdapterModel> {
        val binding = ItemUserCircleBinding.inflate(layoutInflater, parent, false)
        if (isWrapContent) {
            val newLayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            newLayoutParams.setMargins(4.toDp, 4.toDp, 4.toDp, 4.toDp)
            binding.root.layoutParams = newLayoutParams
        }
        return UserHorizontalViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<UserAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: UserAdapterModel,
            newItem: UserAdapterModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: UserAdapterModel,
            newItem: UserAdapterModel
        ) = oldItem == newItem
    }

}

class UserHorizontalViewHolder(
    binding: ItemUserCircleBinding,
) : BaseViewHolder<ItemUserCircleBinding, UserAdapterModel>(binding) {

    override fun onBind(item: UserAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        val fullName = "${item.name}\n${item.lastName}"
        title.text = fullName
        setupPoster()
    }

    private fun setupPoster() = itemView.context.showImage(
        uri = item.imageUrl,
        imageView = binding.poster
    )

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.userItemOnClickListener(userId = item.id)
        }
    }
}
