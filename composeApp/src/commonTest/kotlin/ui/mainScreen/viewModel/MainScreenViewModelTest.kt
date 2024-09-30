import androidx.compose.runtime.mutableStateOf

import data.MockPizzaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ui.mainScreen.viewModel.MainScreenViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    // Create an instance of MainScreenViewModel
    private lateinit var viewModel: MainScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = MainScreenViewModel()
        viewModel.currentPizza = mutableStateOf(MockPizzaData.pizzas.first())
    }

    @AfterTest
    fun tearDown() {
        // Reset the Main dispatcher to the original Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `test setPizzaSelection updates isPizzaSelected`() = runTest {
        // Initially, isPizzaSelected should be false
        assertEquals(false, viewModel.isPizzaSelected.value)

        // Call the setPizzaSelection method to set the value to true
        viewModel.setPizzaSelection(true)
        // Run the pending coroutines
        advanceUntilIdle()

        // Check if isPizzaSelected is updated to true
        assertEquals(true, viewModel.isPizzaSelected.value)
    }


    @Test
    fun `test add pizza supplement - pizza pizza supplement added  `() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])

        // Run the pending coroutines
        advanceUntilIdle()
        // Then
        assertEquals(MockPizzaData.supplements[1], viewModel.currentSupplements.value.last())
    }

    @Test
    fun `test remove last pizza supplement - last pizza supplement removed  `() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])

        viewModel.removeLastSupplement()

        // Run the pending coroutines
        advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.currentSupplements.value.size)
    }


    @Test
    fun `test clear pizza supplements - all pizza supplement removed  `() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])

        viewModel.clearSupplements()

        // Run the pending coroutines
        advanceUntilIdle()

        // Then
        assertEquals(0, viewModel.currentSupplements.value.size)
    }


}
