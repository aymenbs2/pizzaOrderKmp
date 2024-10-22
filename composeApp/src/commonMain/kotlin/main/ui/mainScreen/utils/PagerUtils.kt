package com.aymendev.pizzaorder.ui.utils

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

    //this cover animations cases : {when scroll left,right, enter to details page,exist from details page}
    //This to calculate pizza rotation based on the current state
    //if we are in scroll state left or right we take the rotation from pager offset else when click on the
    //pizza item we will take the scroll from external source(in our case animation source)
     fun getPizzaItemRotationValue(
        isScrollInProgress: Boolean,
        pageOffset: Float,
        rotation: Float?
    ): Float {
         val isCurrentItem = pageOffset < 0.5f
        val rotationValue = if (isScrollInProgress && isCurrentItem) {
            100 * -pageOffset
        } else
            // pageOffset < 0.5f it will take just the center item when come back from detail page and rotate it
            if (isCurrentItem) {
            rotation ?: 0f
        } else {
            0f
        }
        return rotationValue
    }

}