import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import data.MockPizzaData
import org.jetbrains.compose.ui.tooling.preview.Preview

import ui.mainScreen.view.MainScreen
import unitTest.viewModel.MainScreenViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        println("ddd no density=${16.dp.value}")
        val viewModel = MainScreenViewModel()
        viewModel.setCurrentPizza( MockPizzaData.pizzas.first())
        MainScreen(viewModel)
    }
}