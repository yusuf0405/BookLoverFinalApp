package com.example.bookloverfinalapp.app.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.domain.Mapper

class GenericAdapter(private val actionListener: ItemOnClickListener) :
    RecyclerView.Adapter<GenericViewHolder>(), Mapper<List<ItemUi>, Unit> {

    private var itemList = mutableListOf<ItemUi>()

    override fun map(from: List<ItemUi>) {
        val diffUtilCallback = DiffUtilCallback(itemList, from)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        itemList.clear()
        itemList.addAll(from)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int = itemList[position].type()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder =
        ViewHolderChain().execute(viewType, parent, actionListener)

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) =
        holder.bind(itemList[position])

    override fun getItemCount(): Int = itemList.size

    fun addItem(newItem: ItemUi) {
        itemList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun deleteItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, newItem: ItemUi) {
        itemList[position] = newItem
        notifyItemChanged(position)
    }

    fun checkForEmptiness(): Boolean = itemList.isEmpty()

}

interface ItemOnClickListener {

    fun showAnotherFragment(item: ItemUi) = Unit

    fun deleteItem(item: ItemUi, position: Int) = Unit

    fun updateItem(item: ItemUi, position: Int) = Unit
}

abstract class GenericViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: ItemUi)

    companion object {
        const val VIEW_POSITION_ZERO = 0
        const val VIEW_POSITION_FIRST = 1
        const val VIEW_POSITION_SECOND = 2
        const val VIEW_POSITION_THIRD = 3
        const val VIEW_POSITION_FOURTH = 4
        const val VIEW_POSITION_FIFTH = 5
        const val VIEW_POSITION_SIXTH = 6
        const val VIEW_POSITION_SEVENTH = 7
    }
}