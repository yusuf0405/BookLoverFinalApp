package com.joseph.stories.presentation.adapter

data class RecyclerRowPadding(
    var start:Int? = null,
    var top:Int? = null,
    var end:Int? = null,
    var bottom:Int? = null,
) {
    constructor(all:Int): this(all, all, all, all)
}
