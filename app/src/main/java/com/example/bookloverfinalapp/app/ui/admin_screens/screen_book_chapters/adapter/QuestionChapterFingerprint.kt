package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.ItemChapterBinding


class QuestionChapterFingerprint :
    ItemFingerprint<ItemChapterBinding, QuestionChapterAdapterModel> {

    override fun isRelativeItem(item: Item) = item is QuestionChapterAdapterModel

    override fun getLayoutId() = R.layout.item_chapter

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemChapterBinding, QuestionChapterAdapterModel> {
        val binding = ItemChapterBinding.inflate(layoutInflater, parent, false)
        return SavedBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<QuestionChapterAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: QuestionChapterAdapterModel,
            newItem: QuestionChapterAdapterModel
        ) = oldItem.title == newItem.title

        override fun areContentsTheSame(
            oldItem: QuestionChapterAdapterModel,
            newItem: QuestionChapterAdapterModel
        ) = oldItem == newItem
    }

}

class SavedBookViewHolder(
    binding: ItemChapterBinding,
) : BaseViewHolder<ItemChapterBinding, QuestionChapterAdapterModel>(binding) {

    override fun onBind(item: QuestionChapterAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        chapterText.text = item.title
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.actionListener.chapterItemOnClickListener(currentChapterPosition = item.chapter)
        }
    }
}
