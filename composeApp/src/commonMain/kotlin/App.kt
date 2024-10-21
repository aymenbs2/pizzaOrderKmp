import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import main.data.MockPizzaData
import org.jetbrains.compose.ui.tooling.preview.Preview

import main.ui.mainScreen.view.MainScreen
import main.ui.viewModel.MainScreenViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = MainScreenViewModel()
        viewModel.setCurrentPizza(MockPizzaData.pizzas.first())
        MainScreen(viewModel)
    }
}
