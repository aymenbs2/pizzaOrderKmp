package main.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aymendev.pizzaorder.data.PizzaComposition
import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.data.PizzaSupplement
import main.data.MockPizzaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import main.ui.mainScreen.MainScreenUiState

class MainScreenViewModel : ViewModel() {

    private val _isPizzaSelected = MutableStateFlow(false)
    private val _isPizzaBoxClosed = MutableStateFlow(false)
    private val _isPizzaBoxVisible = MutableStateFlow(false)

    private val _currentSupplements = MutableStateFlow(mutableListOf<PizzaSupplement>())
    private val _currentPizza = MutableStateFlow(MockPizzaData.pizzas.first())

    //layout values
    private var _pizzaLayoutCoordinates = MutableStateFlow(Any())
    private val _plateSize = MutableStateFlow(0.dp)
    private val _cornerRadiusBg = MutableStateFlow(0.dp)
    private val _selectedRotation = MutableStateFlow(0f)
    private val _rotation = MutableStateFlow(0F)

    var pizzaComposition: PizzaComposition? = null

    private val _uiState = MutableStateFlow(
        MainScreenUiState(
            isPizzaSelected = _isPizzaSelected.value,
            isPizzaBoxVisible = _isPizzaBoxVisible.value,
            isPizzaBoxClosed = _isPizzaBoxClosed.value,
            currentPizza = _currentPizza.value,
            currentSupplements = _currentSupplements.value,
            pizzaLayoutCoordinates = _pizzaLayoutCoordinates.value,
            plateSize = _plateSize.value,
            cornerRadiusBg = _cornerRadiusBg.value,
            isDisplaySupplementsAllowed = !_isPizzaBoxVisible.value,
            selectedRotation = _selectedRotation.value,
            rotation = _rotation.value,
        )
    )
    val uiState: StateFlow<MainScreenUiState> = _uiState
    fun setPizzaSelection(value: Boolean) {
        viewModelScope.launch {
            _isPizzaSelected.value = value
            updateUiState()
        }
    }


    fun onPizzaItemClicked() {

        if (pizzaComposition == null) {
            pizzaComposition = PizzaComposition()
        }
        pizzaComposition?.pizza = _currentPizza.value
        setPizzaSelection(true)

    }

    fun showPizzaBox() {
        viewModelScope.launch {
            _isPizzaBoxVisible.value = true
            _isPizzaBoxClosed.value = true
            updateUiState()
        }
    }

    fun setCurrentPizza(pizza: Pizza) {
        viewModelScope.launch {
            _currentPizza.value = pizza
            updateUiState()
        }
    }


    fun hidePizzaBox() {
        viewModelScope.launch {
            _isPizzaBoxVisible.value = false
            _isPizzaBoxClosed.value = false
            updateUiState()
        }
    }

    fun setPizzaUiCoordinate(coordinates: LayoutCoordinates) {
        viewModelScope.launch {
            _pizzaLayoutCoordinates.value = coordinates
            updateUiState()
        }
    }

    fun addSupplement(supplement: PizzaSupplement) {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf<PizzaSupplement>().apply {
                addAll(_currentSupplements.value)
                add(supplement)
            }
            updateUiState()
        }
    }

    fun removeLastSupplement() {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf<PizzaSupplement>().apply {
                addAll(_currentSupplements.value)
                removeLast()
            }
            updateUiState()
        }
    }

    fun clearSupplements() {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf()
            updateUiState()
        }
    }

    private fun updateUiState() {
        _uiState.value = MainScreenUiState(
            isPizzaSelected = _isPizzaSelected.value,
            isPizzaBoxVisible = _isPizzaBoxVisible.value,
            isPizzaBoxClosed = _isPizzaBoxClosed.value,
            currentPizza = _currentPizza.value,
            currentSupplements = _currentSupplements.value,
            pizzaLayoutCoordinates = _pizzaLayoutCoordinates.value,
            plateSize = _plateSize.value,
            cornerRadiusBg = _cornerRadiusBg.value,
            isDisplaySupplementsAllowed = !_isPizzaBoxVisible.value,
            selectedRotation = _selectedRotation.value,
            rotation = _rotation.value,
        )
    }

    fun setContainerSize(containerSize: State<Dp>) {
        viewModelScope.launch {
            _plateSize.value = containerSize.value
            updateUiState()
        }
    }

    fun setCornerRadiusBg(cornerRadius: State<Dp>) {
        viewModelScope.launch {
            _cornerRadiusBg.value = cornerRadius.value
            updateUiState()
        }
    }

    fun setSelectedRotation(selectRotation: State<Float>) {
        viewModelScope.launch {
            _selectedRotation.value = selectRotation.value
            updateUiState()
        }
    }


    fun setRotation(rotation: State<Float>) {
        viewModelScope.launch {
            _rotation.value = rotation.value
            updateUiState()
        }
    }

}




