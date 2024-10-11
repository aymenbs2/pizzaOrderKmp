package automated

import androidx.compose.ui.test.*
import automated.pages.MainScreenPage
import ui.mainScreen.view.MainScreen
import unitTest.viewModel.MainScreenViewModel
import kotlin.test.Test

class MainAutomatedTest {

    private val viewModel = MainScreenViewModel()
    private lateinit var mainScreenPage: MainScreenPage


    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test_add_pizza_to_cart() = runComposeUiTest {
        mainScreenPage = MainScreenPage(this)
        // Declares a mock UI to demonstrate API calls
        //
        // Replace with your own declarations to test the code of your project
        setContent {
            MainScreen(viewModel)
        }
        mainScreenPage.assertMainHeaderExist()
        mainScreenPage.swipeLeft()
        mainScreenPage.openPizzaDetails(2)
        mainScreenPage.assertDetailsOpened()
        mainScreenPage.addPizzaToCardFromDetails()
        mainScreenPage.assertPizzaAddedToCart()



    }
}