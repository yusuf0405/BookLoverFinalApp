package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.fingerprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.joseph.ui_core.adapter.BaseViewHolder
import com.joseph.ui_core.adapter.Item
import com.joseph.ui_core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models.SearchAdapterModel
import com.example.bookloverfinalapp.app.utils.extensions.setupSearchViewParams
import com.example.bookloverfinalapp.databinding.ItemSearchViewBinding


class SearchFingerprint : ItemFingerprint<ItemSearchViewBinding, SearchAdapterModel> {

    override fun isRelativeItem(item: Item) = item is SearchAdapterModel

    override fun getLayoutId() = R.layout.item_search_view

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemSearchViewBinding, SearchAdapterModel> {
        val binding = ItemSearchViewBinding.inflate(layoutInflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<SearchAdapterModel>() {

        override fun areItemsTheSame(oldItem: SearchAdapterModel, newItem: SearchAdapterModel) =
           true

        override fun areContentsTheSame(oldItem: SearchAdapterModel, newItem: SearchAdapterModel) =
            true
    }

}

class SearchViewHolder(
    binding: ItemSearchViewBinding,
) : BaseViewHolder<ItemSearchViewBinding, SearchAdapterModel>(binding) {

    override fun onBind(item: SearchAdapterModel) {
        super.onBind(item)
        binding.searchHeader.setupSearchViewParams()
        if (item.queryText.isNotEmpty()){
            binding.searchHeader.queryHint = item.queryText
        }
        binding.searchHeader.setOnQueryTextListener(item.listener)
    }

}

