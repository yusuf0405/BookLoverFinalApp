package com.joseph.ui_core.custom.scroll_button

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joseph.ui_core.R
import kotlin.math.abs


private const val MIN_VERTICAL_SCROLL_POWER = 2
private const val MIN_RANGE_ITEMS_TO_SMOOTH = 20
private const val MIN_SCROLLED_ITEMS_TO_SHOW_BUTTON = 2

open class ScrollButton @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attributeSet, defStyleAttr) {

    enum class ScrollDirection {
        UP, DOWN, UNKNOWN
    }

    private lateinit var recyclerView: RecyclerView

    var lastScrollDirection = ScrollDirection.UNKNOWN

    init {
        setOnClickListener { onClick() }
    }

    open fun onClick() {
        when (lastScrollDirection) {
            ScrollDirection.DOWN -> scrollToUp()
            else -> scrollToDown()
        }
    }

    private fun scrollToUp() {
        var visibleItemPosition = RecyclerView.NO_POSITION
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            visibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }

        if (visibleItemPosition in 1..MIN_RANGE_ITEMS_TO_SMOOTH) {
            recyclerView.smoothScrollToPosition(0)
        } else {
            lastScrollDirection = ScrollDirection.UP
            recyclerView.scrollToPosition(0)
        }
    }

    private fun scrollToDown() {
        val adapter = recyclerView.adapter ?: return

        val toPosition = adapter.itemCount

        var visibleItemPosition = RecyclerView.NO_POSITION
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            visibleItemPosition = layoutManager.findLastVisibleItemPosition()
        }

        if (visibleItemPosition != RecyclerView.NO_POSITION) {
            val range = toPosition - visibleItemPosition
            if (range <= MIN_RANGE_ITEMS_TO_SMOOTH) {
                recyclerView.smoothScrollToPosition(toPosition)
            } else {
                lastScrollDirection = ScrollDirection.DOWN
                recyclerView.scrollToPosition(toPosition)
                // dirty hack, because scrollToPosition doesn't call OnScrollListener.onScrollStateChanged
                recyclerView.smoothScrollToPosition(toPosition)
            }
        }
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val fistItemPosition = layoutManager.findFirstVisibleItemPosition()
                val canShowButton = fistItemPosition >= MIN_SCROLLED_ITEMS_TO_SHOW_BUTTON
                if (canShowButton && !this@ScrollButton.isVisible) showButton()
                calculateScrollDirection(dy)
                updateScrollButton()
            }
        })
    }

    private fun showButton() {
        if (this.isVisible) return
        this.isVisible = true
        with(AnimationUtils.loadAnimation(context, R.anim.scroll_btn_bounce)) {
            startAnimation(this)
        }
    }

    private fun calculateScrollDirection(dy: Int) {
        if (!isEnoughPoweredScrollToChangeDirection(dy)) return
        lastScrollDirection = if (dy > 0) ScrollDirection.DOWN else ScrollDirection.UP
    }

    private fun isEnoughPoweredScrollToChangeDirection(verticalScroll: Int): Boolean {
        return abs(verticalScroll) > MIN_VERTICAL_SCROLL_POWER
    }

    /** Call this method after calculateScrollDirection */
    private fun updateScrollButton() {
        when (lastScrollDirection) {
            ScrollDirection.UP -> setImageResource(R.drawable.ic_button_down_state)
            else -> setImageResource(R.drawable.ic_button_up_state)
        }
    }
}