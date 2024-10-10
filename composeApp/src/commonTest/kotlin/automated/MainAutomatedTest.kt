package automated

import androidx.compose.ui.test.*
import ui.mainScreen.view.MainScreen
import ui.mainScreen.view.MainScreenTestTags
import unitTest.viewModel.MainScreenViewModel
import kotlin.test.Test

class MainAutomatedTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun myTest() = runComposeUiTest {
        // Declares a mock UI to demonstrate API calls
        //
        // Replace with your own declarations to test the code of your project
        setContent {
           val viewModel = MainScreenViewModel()
            MainScreen(viewModel)
        }

        onNodeWithTag(MainScreenTestTags.MAIN_HEADER.name).assertIsDisplayed()
        onAllNodesWithTag(MainScreenTestTags.PIZZA_ITEM.name)[2].performScrollTo()

        onAllNodesWithTag(MainScreenTestTags.PIZZA_ITEM.name)[2].performClick()

        onNodeWithTag(MainScreenTestTags.DETAILS_HEADER.name).assertIsDisplayed()


    }
}