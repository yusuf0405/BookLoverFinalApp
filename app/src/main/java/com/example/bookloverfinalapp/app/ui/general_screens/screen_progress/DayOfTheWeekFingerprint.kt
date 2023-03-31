package com.example.bookloverfinalapp.app.ui.general_screens.screen_progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.adapter.DayOfTheWeekAdapterModel
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.databinding.ItemDayOfTheWeekBinding


class DayOfTheWeekFingerprint : ItemFingerprint<ItemDayOfTheWeekBinding, DayOfTheWeekAdapterModel> {

    override fun isRelativeItem(item: Item) = item is DayOfTheWeekAdapterModel

    override fun getLayoutId() = R.layout.item_day_of_the_week

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemDayOfTheWeekBinding, DayOfTheWeekAdapterModel> {
        val binding = ItemDayOfTheWeekBinding.inflate(layoutInflater, parent, false)
        return DayOfTheWeekHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<DayOfTheWeekAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: DayOfTheWeekAdapterModel,
            newItem: DayOfTheWeekAdapterModel
        ) =
            oldItem.day == newItem.day

        override fun areContentsTheSame(
            oldItem: DayOfTheWeekAdapterModel,
            newItem: DayOfTheWeekAdapterModel
        ) = oldItem == newItem
    }

}

class DayOfTheWeekHolder(
    binding: ItemDayOfTheWeekBinding,
) : BaseViewHolder<ItemDayOfTheWeekBinding, DayOfTheWeekAdapterModel>(binding) {

    override fun onBind(item: DayOfTheWeekAdapterModel) {
        super.onBind(item)
        with(binding) {
            if (item.day == dayText.context.getString(R.string.your_point_this_week)) {
                dayText.textSize = 18F
                progressText.textSize = 18F
            }
            dayText.text = item.day
            progressText.text = item.progress.toString()
        }
    }
}

