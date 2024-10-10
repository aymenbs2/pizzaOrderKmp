package automated.pages

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import ui.mainScreen.view.MainScreenTestTags

class MainScreenPage @OptIn(ExperimentalTestApi::class) constructor(val uiTest: ComposeUiTest) {

    @OptIn(ExperimentalTestApi::class)
    fun assertMainHeaderExist() {
        uiTest.onNodeWithTag(MainScreenTestTags.MAIN_HEADER.name).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun openPizzaDetails(index:Int){
        uiTest.onAllNodesWithTag(MainScreenTestTags.PIZZA_ITEM.name)[index].performScrollTo()
        uiTest.onAllNodesWithTag(MainScreenTestTags.PIZZA_ITEM.name)[index].performClick()
        uiTest.onNodeWithTag(MainScreenTestTags.DETAILS_HEADER.name).assertIsDisplayed()

    }


    @OptIn(ExperimentalTestApi::class)
    fun addPizzaToCardFromDetails(index:Int){
        uiTest.onAllNodesWithTag(MainScreenTestTags.ADD_TO_CART_BUTTON.name)[index].performClick()

    }
}