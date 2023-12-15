package com.joseph.core.extensions

import java.util.*

fun Date.startOfDay() = Date(this.year, this.month, this.date)

fun Date.addDays(days: Int) =
    Date(this.year, this.month, this.date + days, this.hours, this.minutes, this.seconds)
