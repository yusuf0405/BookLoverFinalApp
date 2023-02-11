package com.example.bookloverfinalapp.app.ui.adapter.animations.custom

import androidx.recyclerview.widget.RecyclerView
import com.broadcast.myapplication.adapter.animations.custom.CommonItemAnimator

class SlideInTopCommonAnimator: CommonItemAnimator {

    override fun preAnimateAdd(holder: RecyclerView.ViewHolder) {
        holder.itemView.translationY = -holder.itemView.height.toFloat() * 2
        holder.itemView.alpha = 0f
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().translationY(0f)
        holder.itemView.animate().alpha(1f)
    }


    override fun animateRemove(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().translationY(-holder.itemView.height.toFloat())
    }

}