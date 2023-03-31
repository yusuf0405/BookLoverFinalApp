package com.joseph.ui_core.adapter.managers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/** Items preview width in percent where 1.0 equals 100% width */
private const val ItemPeekingPercent = 0.915f

/** This value determines where items reach the final (minimum) scale */
private const val ScaleDistanceFactor = 1.5f

/** The final (minimum) scale for non-prominent items is 1-[Coefficient] */

private const val Coefficient: Float = 0.10f
class PeekingLinearLayoutManager @JvmOverloads constructor(
    context: Context?,
    @RecyclerView.Orientation orientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun generateDefaultLayoutParams() =
        scaledLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?) =
        scaledLayoutParams(super.generateLayoutParams(lp))

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?) =
        scaledLayoutParams(super.generateLayoutParams(c, attrs))

    private fun scaledLayoutParams(params: RecyclerView.LayoutParams) = params.apply {
        when (orientation) {
            RecyclerView.HORIZONTAL -> {
                width = (calculateHorizontalSpace() * ItemPeekingPercent).toInt()
            }
            RecyclerView.VERTICAL -> {
                height = (calculateVerticalSpace() * ItemPeekingPercent).toInt()
            }
        }
    }

    private fun calculateHorizontalSpace() = width - paddingStart - paddingEnd

    private fun calculateVerticalSpace() = height - paddingTop - paddingBottom

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleDownItems() }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleDownItems()
    }

    private fun scaleDownItems() {
        val threshold = ScaleDistanceFactor * (width / 2)

        forEachChild { child ->
            val childCenter = (child.left + child.right) / 2f
            val distanceToCenter = abs(childCenter - (width / 2f))
            val scaleAmount = (distanceToCenter / threshold).coerceAtMost(1f)
            child.scaleY = 1 - (Coefficient * scaleAmount)
        }
    }

    private fun forEachChild(action: (View) -> Unit) {
        for (i in 0 until childCount) getChildAt(i)?.let(action)
    }
}