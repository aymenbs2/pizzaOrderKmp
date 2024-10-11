package main.ui.mainScreen

import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.data.PizzaSupplement

data class MainScreenUiState(
    val isPizzaSelected: Boolean = false,
    val isPizzaBoxVisible: Boolean = false,
    val isPizzaBoxClosed: Boolean = false,
    val isDisplaySupplementsAllowed: Boolean = false,
    val currentPizza: Pizza,
    val currentSupplements: List<PizzaSupplement>,
    val pizzaLayoutCoordinates: Any? = null,
    val plateSize: State<Dp>,
    val selectedRotation:  State<Float>,
    val rotation:  State<Float>,
    val cornerRadiusBg: State<Dp>,
)