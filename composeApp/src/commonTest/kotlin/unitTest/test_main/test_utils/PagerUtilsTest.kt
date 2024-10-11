package unitTest.ui.utils

import com.aymendev.pizzaorder.ui.utils.PagerUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class PagerUtilsTest {

    @Test
    fun calculatePageOffsetCurrentPageReturns_currentPageOffsetFractionAbsoluteValue() {
        val currentPageOffsetFraction = -0.5f
        val currentPage = 2
        val page = 2
        assertEquals(
            0.5f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun calculatePageOffsetCurrentPage_returns_1_plusCurrentPageOffsetFractionWhenNegative() {

        val currentPageOffsetFraction = -0.3f
        val currentPage = 3
        val page = 2
        assertEquals(
            0.7f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun calculatePageOffsetCurrentPagePlus1Returns1MinusCurrentPageOffsetFractionWhenPositive() {
        val currentPageOffsetFraction = 0.7f
        val currentPage = 3
        val page = 4
        assertEquals(
            0.3f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun calculatePageOffsetOtherPage_returns1() {
        val currentPageOffsetFraction = 0.2f
        val currentPage = 2
        val page = 4
        assertEquals(
            1f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }
}



