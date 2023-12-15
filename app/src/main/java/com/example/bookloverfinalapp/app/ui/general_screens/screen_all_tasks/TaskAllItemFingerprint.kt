package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.TaskAdapterModel
import com.example.bookloverfinalapp.app.utils.genre.GenreOnClickListener
import com.example.bookloverfinalapp.databinding.ItemLittileTaskBinding
import com.example.bookloverfinalapp.R

class TaskAllItemFingerprint : ItemFingerprint<ItemLittileTaskBinding, TaskAdapterModel> {

    override fun isRelativeItem(item: Item) = item is TaskAdapterModel

    override fun getLayoutId() = R.layout.item_task

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemLittileTaskBinding, TaskAdapterModel> {
        val binding = ItemLittileTaskBinding.inflate(layoutInflater, parent, false)
        return TaskLittleViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<TaskAdapterModel>() {

        override fun areItemsTheSame(oldItem: TaskAdapterModel, newItem: TaskAdapterModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TaskAdapterModel, newItem: TaskAdapterModel) =
            oldItem == newItem
    }

}

class TaskLittleViewHolder(
    binding: ItemLittileTaskBinding,
) : BaseViewHolder<ItemLittileTaskBinding, TaskAdapterModel>(binding), GenreOnClickListener {

    override fun onBind(item: TaskAdapterModel) {
        super.onBind(item)
        with(binding) {
            title.text = item.title
            description.text = item.description
            time.text = item.getDate()
//            binding.genreLayout.removeAllViews()
        }
    }
}

