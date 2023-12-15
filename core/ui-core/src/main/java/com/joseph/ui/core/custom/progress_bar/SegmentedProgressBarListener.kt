package com.joseph.ui.core.custom.progress_bar

interface SegmentedProgressBarListener {

    fun onPage(oldPageIndex: Int, newPageIndex: Int)

    fun onFinished()
}