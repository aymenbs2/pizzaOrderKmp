package com.aymendev.pizzaorder.ui.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

object PagerUtils {
    var currentIndex = 0

    fun calculatePageOffset(currentPageOffsetFraction: Float, currentPage: Int, page: Int): Float {
        return when (page) {
            currentPage -> currentPageOffsetFraction.absoluteValue
            currentPage - 1 -> 1 + currentPageOffsetFraction.coerceAtMost(0f)
            currentPage + 1 -> 1 - currentPageOffsetFraction.coerceAtLeast(0f)
            else -> 1f
        }
    }


}