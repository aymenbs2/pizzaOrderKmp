import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import data.MockPizzaData
import org.jetbrains.compose.ui.tooling.preview.Preview

import ui.mainScreen.view.MainScreen
import ui.mainScreen.viewModel.MainScreenViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = MainScreenViewModel()
        viewModel.currentPizza.value = MockPizzaData.pizzas.first()
        MainScreen(viewModel)
    }
}