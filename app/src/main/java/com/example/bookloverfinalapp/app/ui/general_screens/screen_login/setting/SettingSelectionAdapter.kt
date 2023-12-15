package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.joseph.ui.core.databinding.ItemSelectionSettingBinding


class SettingSelectionAdapter(
    private val onItemSelectionListener: OnItemSelectionListener
) : RecyclerView.Adapter<SettingSelectionAdapter.SelectionViewHolder>() {

    private val items = arrayListOf<SettingSelectionItem>()

    fun populate(newItems: List<SettingSelectionItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectionSettingBinding.inflate(inflater, parent, false)
        return SelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class SelectionViewHolder(private val binding: ItemSelectionSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SettingSelectionItem) {
            with(binding) {
                title.text = item.title
                title.isVisible = item.title.isNotEmpty()
                selector.isVisible = item.isChecked

                container.setOnClickListener {
                    for (selectionItem in items) {
                        selectionItem.isChecked = false
                    }

                    item.isChecked = true
                    notifyDataSetChanged()
                    onItemSelectionListener.onItemSelected(item)
                }
            }
        }
    }

    interface OnItemSelectionListener {

        fun onItemSelected(item: SettingSelectionItem)
    }
}