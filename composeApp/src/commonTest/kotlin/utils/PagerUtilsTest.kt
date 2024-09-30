package utils

import com.aymendev.pizzaorder.ui.utils.PagerUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class PagerUtilsTest {

    @Test
    fun `calculatePageOffset currentPage returns currentPageOffsetFraction absolute value`() {
        val currentPageOffsetFraction = -0.5f
        val currentPage = 2
        val page = 2
        assertEquals(
            0.5f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun `calculatePageOffset currentPage-1 returns 1 plus currentPageOffsetFraction when negative`() {

        val currentPageOffsetFraction = -0.3f
        val currentPage = 3
        val page = 2
        assertEquals(
            0.7f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun `calculatePageOffset currentPage+1 returns 1 minus currentPageOffsetFraction when positive`() {
        val currentPageOffsetFraction = 0.7f
        val currentPage = 3
        val page = 4
        assertEquals(
            0.3f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }

    @Test
    fun `calculatePageOffset other page returns 1`() {
        val currentPageOffsetFraction = 0.2f
        val currentPage = 2
        val page = 4
        assertEquals(
            1f,
            PagerUtils.calculatePageOffset(currentPageOffsetFraction, currentPage, page)
        )
    }
}



