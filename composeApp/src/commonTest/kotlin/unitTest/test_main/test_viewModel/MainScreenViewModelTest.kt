package unitTest.ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp

import main.data.MockPizzaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import main.ui.viewModel.MainScreenViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    // Create an instance of MainScreenViewModel
    private lateinit var viewModel: MainScreenViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = MainScreenViewModel()
        viewModel.setCurrentPizza(MockPizzaData.pizzas.first())
    }

    @AfterTest
    fun tearDown() {
        // Reset the Main dispatcher to the original Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun testSetPizzaSelection_updatesIsPizzaSelected() = runTest {

        // Call the setPizzaSelection method to set the value to true
        viewModel.setPizzaSelection(true)
        // Run the pending coroutines
        advanceUntilIdle()

        // Check if isPizzaSelected is updated to true
        assertEquals(true, viewModel.uiState.value.isPizzaSelected)
    }


    @Test
    fun testAddPizzaSupplement_pizza_supplementAdded() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])
        // Run the pending coroutines
        advanceUntilIdle()
        // Then
        assertEquals(MockPizzaData.supplements[1], viewModel.uiState.value.currentSupplements.last())
    }

    @Test
    fun testRemoveLastPizzaSupplement_lastPizzasupplementremoved() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])
        advanceUntilIdle()
        viewModel.removeLastSupplement()

        // Run the pending coroutines
        advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.uiState.value.currentSupplements.size)
    }


    @Test
    fun testClearPizzaSupplements_allPizzaSupplementRemoved() = runTest {

        //when
        viewModel.addSupplement(MockPizzaData.supplements.first())
        viewModel.addSupplement(MockPizzaData.supplements[1])

        viewModel.clearSupplements()

        // Run the pending coroutines
        advanceUntilIdle()

        // Then;
        assertEquals(0, viewModel.uiState.value.currentSupplements.size)
    }

    @Test
    fun testOnPizzaItemClicked_pizzaSelected() = runTest {

        //when
        viewModel.onPizzaItemClicked()

        // Run the pending coroutines
        advanceUntilIdle()

        // Then
        assertEquals(true, viewModel.uiState.value.isPizzaSelected)
        assertNotNull( viewModel.pizzaComposition)
    }
    @Test
    fun testShowPizzaShowed_uiStateShowBox() = runTest {


        //when
        viewModel.showPizzaBox()

        // Run the pending coroutines
        advanceUntilIdle()
      //Then
        assertEquals(true, viewModel.uiState.value.isPizzaBoxVisible)
        assertEquals(true, viewModel.uiState.value.isPizzaBoxClosed)
    }

    @Test
    fun testHidePizzaShowed_uiStateHiddenBox() = runTest {


        //when
        viewModel.hidePizzaBox()

        // Run the pending coroutines
        advanceUntilIdle()
      //Then
        assertEquals(false, viewModel.uiState.value.isPizzaBoxVisible)
        assertEquals(false, viewModel.uiState.value.isPizzaBoxClosed)
    }

    @Test
    fun testCurrentPizzaSetter_setCurrentPizzaValue() = runTest {
        //given
        val newCurrentPizza= MockPizzaData.pizzas[2]
        //when
        viewModel.setCurrentPizza(newCurrentPizza)

        // Run the pending coroutines
        advanceUntilIdle()
        //Then
        assertEquals(newCurrentPizza, viewModel.uiState.value.currentPizza)
    }

    @Test
    fun testUiStateDataUpdate_uiDataStateUpdate() = runTest {
        //given
        val containerSize= mutableStateOf(100.dp)
        val cornerRadiusBg= mutableStateOf(40.dp)
        val selectedRotation= mutableStateOf(40F)
        val rotation= mutableStateOf(50F)
        //when
        viewModel.setContainerSize(containerSize)
        viewModel.setCornerRadiusBg(cornerRadiusBg)
        viewModel.setSelectedRotation(selectedRotation)
        viewModel.setRotation(rotation)

        // Run the pending coroutines
        advanceUntilIdle()
        //Then
        assertEquals(containerSize.value, viewModel.uiState.value.plateSize)
        assertEquals(cornerRadiusBg.value, viewModel.uiState.value.cornerRadiusBg)
        assertEquals(selectedRotation.value, viewModel.uiState.value.selectedRotation)
        assertEquals(rotation.value, viewModel.uiState.value.rotation)
    }

}
