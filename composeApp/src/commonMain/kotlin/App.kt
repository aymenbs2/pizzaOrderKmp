import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.core.AnimatedCircularImages
import org.jetbrains.compose.ui.tooling.preview.Preview

import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.pizza
import ui.viewModels.MainViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen(MainViewModel())
    }
}