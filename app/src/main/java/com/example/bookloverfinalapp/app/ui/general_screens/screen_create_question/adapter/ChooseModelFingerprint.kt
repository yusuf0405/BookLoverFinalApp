package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter.QuestionChapterAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.models.ChooseAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.getAttrColor
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemChapterBinding


class ChooseModelFingerprint : ItemFingerprint<ItemChapterBinding, ChooseAdapterModel> {

    override fun isRelativeItem(item: Item) = item is ChooseAdapterModel

    override fun getLayoutId() = R.layout.item_chapter

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemChapterBinding, ChooseAdapterModel> {
        val binding = ItemChapterBinding.inflate(layoutInflater, parent, false)
        return SavedBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ChooseAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: ChooseAdapterModel,
            newItem: ChooseAdapterModel
        ) = false

        override fun areContentsTheSame(
            oldItem: ChooseAdapterModel,
            newItem: ChooseAdapterModel
        ) = false
    }
}

class SavedBookViewHolder(
    binding: ItemChapterBinding,
) : BaseViewHolder<ItemChapterBinding, ChooseAdapterModel>(binding) {

    override fun onBind(item: ChooseAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        chapterText.text = item.title
        val context = root.context
        val color = if (item.isChecked) context.getColor(R.color.blue)
        else context.getAttrColor(R.attr.card_background)
        root.setBackgroundColor(color)
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.listener.chooseModelOnClickListener(item = item)
        }
    }
}
