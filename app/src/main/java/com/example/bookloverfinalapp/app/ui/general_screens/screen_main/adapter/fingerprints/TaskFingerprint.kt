package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.R as UiCore
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.TaskAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemTaskBinding
import com.joseph.ui.core.extensions.toDp
import com.example.bookloverfinalapp.R

class TaskFingerprint : ItemFingerprint<ItemTaskBinding, TaskAdapterModel> {

    override fun isRelativeItem(item: Item) = item is TaskAdapterModel

    override fun getLayoutId() = R.layout.item_task

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTaskBinding, TaskAdapterModel> {
        val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
        binding.root.layoutParams = binding.root.layoutParams.also {
            it.width = 280.toDp
        }
        return TaskViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<TaskAdapterModel>() {

        override fun areItemsTheSame(oldItem: TaskAdapterModel, newItem: TaskAdapterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskAdapterModel, newItem: TaskAdapterModel) =
            oldItem == newItem
    }

}

class TaskViewHolder(
    binding: ItemTaskBinding,
) : BaseViewHolder<ItemTaskBinding, TaskAdapterModel>(binding) {

    override fun onBind(item: TaskAdapterModel) {
        super.onBind(item)
        with(binding) {
            container.startSlideInLeftAnim()
            title.text = item.title
            description.text = item.description
            time.text = item.getDate()
//            binding.genreLayout.removeAllViews()
        }
    }
}

