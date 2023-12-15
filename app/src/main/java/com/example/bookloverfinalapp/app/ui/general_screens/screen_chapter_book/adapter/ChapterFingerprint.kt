package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.joseph.ui.core.adapter.BaseViewHolder
import com.joseph.ui.core.adapter.Item
import com.joseph.ui.core.adapter.ItemFingerprint
import com.example.bookloverfinalapp.app.utils.extensions.makeTicker
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.startSlideInLeftAnim
import com.example.bookloverfinalapp.databinding.ItemChapterBinding
import com.example.bookloverfinalapp.R

class ChapterFingerprint : ItemFingerprint<ItemChapterBinding, ChapterAdapterModel> {

    override fun isRelativeItem(item: Item) = item is ChapterAdapterModel

    override fun getLayoutId() = R.layout.item_chapter

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemChapterBinding, ChapterAdapterModel> {
        val binding = ItemChapterBinding.inflate(layoutInflater, parent, false)
        return SavedBookViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<ChapterAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: ChapterAdapterModel,
            newItem: ChapterAdapterModel
        ) = oldItem.chapter.title == newItem.chapter.title

        override fun areContentsTheSame(
            oldItem: ChapterAdapterModel,
            newItem: ChapterAdapterModel
        ) = oldItem == newItem
    }

}

class SavedBookViewHolder(
    binding: ItemChapterBinding,
) : BaseViewHolder<ItemChapterBinding, ChapterAdapterModel>(binding) {

    override fun onBind(item: ChapterAdapterModel) {
        super.onBind(item)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        container.startSlideInLeftAnim()
        chapterText.text = item.chapter.title
        root.isEnabled = item.chapterIsRead
        val context = chapterText.context
        chapterText.makeTicker()
        if (item.chapterIsRead.not()) chapterText.setTextColor(Color.GRAY)
        else {
            val attrs = intArrayOf(com.joseph.ui.core.R.attr.blackOrWhiteColor)
            val array = context.obtainStyledAttributes(attrs)
            chapterText.setTextColor(array.getColor(0, 0))
        }
    }

    private fun setOnClickListeners() = with(binding) {
        root.setOnDownEffectClickListener {
            item.actionListener.chapterItemOnClickListener(
                chapter = item.chapter,
                currentChapterPosition = item.currentChapterPosition
            )
        }
    }
}
