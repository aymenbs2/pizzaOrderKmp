package automated.pages

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.waitUntilAtLeastOneExists

import main.ui.mainScreen.view.MainScreenTestTags

class MainScreenPage @OptIn(ExperimentalTestApi::class) constructor(val uiTest: ComposeUiTest) {

    @OptIn(ExperimentalTestApi::class)
    fun assertMainHeaderExist() {
        uiTest.onNodeWithTag(MainScreenTestTags.MAIN_HEADER.name).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertDetailsOpened() {
        uiTest.waitUntilAtLeastOneExists(
            matcher = hasTestTag(
                MainScreenTestTags.DETAILS_PIZZA.name
            ),
            timeoutMillis = 2_000L,
        )
    }

    @OptIn(ExperimentalTestApi::class)
    fun openPizzaDetails(index: Int) {
        uiTest.onNodeWithTag("${MainScreenTestTags.PIZZA_ITEM.name}_$index")
            .performClick()
    }


    @OptIn(ExperimentalTestApi::class)
    fun addPizzaToCardFromDetails() {
        uiTest.onNodeWithTag(MainScreenTestTags.ADD_TO_CART_BUTTON.name).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun swipeRight() {
        uiTest.onNodeWithTag(MainScreenTestTags.PAGER.name).performTouchInput {
            swipeRight()
        }

    }

    @OptIn(ExperimentalTestApi::class)
    fun swipeLeft() {
        uiTest.onNodeWithTag(MainScreenTestTags.PAGER.name).performTouchInput {
            swipeLeft()
        }
        uiTest.waitUntilAtLeastOneExists(
            hasTestTag(
                "${MainScreenTestTags.PIZZA_ITEM.name}_2"
            )
        )

    }

    @OptIn(ExperimentalTestApi::class)
    fun assertPizzaAddedToCart() {
        uiTest.onNodeWithTag(MainScreenTestTags.PIZZA_COUNT.name)
            .assertTextContains("1")
    }
}