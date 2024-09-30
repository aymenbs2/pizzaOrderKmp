package ui.mainScreen.viewModel

import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aymendev.pizzaorder.data.PizzaComposition
import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.data.PizzaSupplement
import data.MockPizzaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    private var _isPizzaSelected = MutableStateFlow(false)
    val isPizzaSelected = _isPizzaSelected
    private var _isPizzaBoxVisible = MutableStateFlow(false)
    private var _isPizzaBoxClosed = MutableStateFlow(false)
    val isPizzaBoxClosed = _isPizzaBoxClosed
    private val _currentSupplements = MutableStateFlow(mutableListOf<PizzaSupplement>())
    val currentSupplements = _currentSupplements
    private var _pizzaLayoutCoordinates = MutableStateFlow(Any())
    val pizzaLayoutCoordinates = _pizzaLayoutCoordinates
    val isPizzaBoxVisible = _isPizzaBoxVisible
    var pizzaComposition: PizzaComposition? = null
    var currentPizza: MutableState<Pizza> = mutableStateOf(MockPizzaData.pizzas.first())
    lateinit var rootWidth: MutableState<Dp>
    lateinit var cornerRadiusBg: State<Dp>
    lateinit var containerSize: State<Dp>
    lateinit var selectedRotation: State<Float>
    lateinit var rotation: MutableFloatState
    fun setPizzaSelection(value: Boolean) {
        viewModelScope.launch {
            _isPizzaSelected.value = value
        }
    }


    fun onPizzaItemClicked() {
        if (pizzaComposition == null) {
            pizzaComposition = PizzaComposition()
        }
        pizzaComposition?.pizza = currentPizza.value
        setPizzaSelection(true)
    }

    fun showPizzaBox() {
        viewModelScope.launch {
            _isPizzaBoxVisible.value = true
            _isPizzaBoxClosed.value = true
        }


    }


    fun hidePizzaBox() {
        viewModelScope.launch {
            _isPizzaBoxVisible.value = false
            _isPizzaBoxClosed.value = false
        }
    }

    fun getSupplementCircularImagesRadius(i: Int): Int {
        return 40 + i * 20
    }

    fun isDisplaySupplementsAllowed(): Boolean {
        return _currentSupplements.value.size > 0 && !_isPizzaBoxVisible.value && !_isPizzaBoxClosed.value
    }

    fun setPizzaUiCoordinate(coordinates: LayoutCoordinates) {
        viewModelScope.launch {
            _pizzaLayoutCoordinates.value = coordinates
        }
    }

    fun addSupplement(supplement: PizzaSupplement) {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf<PizzaSupplement>().apply {
                addAll(currentSupplements.value)
                add(supplement)
            }
        }
    }

    fun removeLastSupplement() {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf<PizzaSupplement>().apply {
                addAll(  _currentSupplements.value )
                removeLast()
            }
        }
    }
    fun clearSupplements() {
        viewModelScope.launch {
            _currentSupplements.value = mutableListOf()
        }
    }

}




