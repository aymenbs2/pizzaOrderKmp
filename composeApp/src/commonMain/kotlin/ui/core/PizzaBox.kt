package ui.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.base3
import pizzaoderkmp.composeapp.generated.resources.lid3

// Constants for animation and size values
private const val ANIMATION_DURATION = 2000
private const val OFFSET_DELAY = 90
private val BASE_SIZE = 300.dp
private val CLOSED_BOX_SPACER = 250.dp
private val OPENED_BOX_SIZE = 230.dp
private val OPENED_BOX_OFFSET = 150.dp

@Composable
fun PizzaBox(
    modifier: Modifier = Modifier,
    isBoxOpenedToClosed: Boolean,
    isBoxHiddenToVisible: Boolean,
    pizzaImage: DrawableResource,
    onBoxDisappear: () -> Unit
) {
    // Define animations for the pizza box elements
    val spacer = animateDpAsState(
        targetValue = if (isBoxOpenedToClosed) 0.dp else CLOSED_BOX_SPACER,
        animationSpec = tween(ANIMATION_DURATION)
    )
    val size = animateDpAsState(
        targetValue = if (isBoxOpenedToClosed) 0.dp else OPENED_BOX_SIZE,
        animationSpec = tween(ANIMATION_DURATION)
    )

    val offset = animateDpAsState(
        targetValue = if (isBoxOpenedToClosed) OPENED_BOX_OFFSET else 0.dp,
        animationSpec = tween(ANIMATION_DURATION, delayMillis = OFFSET_DELAY)
    )
    val pizzaScale = animateFloatAsState(
        targetValue = if (isBoxOpenedToClosed && isBoxHiddenToVisible) 0f else 1f,
        animationSpec = tween(ANIMATION_DURATION, delayMillis = OFFSET_DELAY),
    )
    val rotation = animateFloatAsState(
        targetValue = if (isBoxOpenedToClosed) 0f else 26f,
        animationSpec = tween(ANIMATION_DURATION)
    )
    LaunchedEffect(
        pizzaScale.value,
        size.value,
        pizzaScale.value,
        rotation.value,
        offset.value,
        spacer.value
    ) {
        if (pizzaScale.value == 0f && size.value == 0.dp && pizzaScale.value == 0f) {
            onBoxDisappear()
        }
    }
    // Animated visibility based on whether the box is visible
    AnimatedVisibility(
        modifier = modifier,
        visible = isBoxHiddenToVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .alpha(pizzaScale.value)
                .offset(x = offset.value, y = -offset.value)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PizzaBoxBase(spacer, rotation, pizzaScale)
            PizzaImage(pizzaImage, spacer, pizzaScale, size)
            PizzaBoxLid(spacer, rotation, pizzaScale)
        }
    }
}

@Composable
private fun PizzaBoxBase(spacer: State<Dp>, rotation: State<Float>, pizzaScale: State<Float>) {
    Image(
        painter = painterResource(Res.drawable.base3),
        contentDescription = "Pizza Box Base",
        modifier = Modifier
            .padding(top = spacer.value / 2)
            .size(BASE_SIZE)
            .graphicsLayer { rotationX = rotation.value }
            .scale(pizzaScale.value)
    )
}

@Composable
private fun PizzaImage(
    pizzaImage: DrawableResource,
    spacer: State<Dp>,
    pizzaScale: State<Float>,
    size: State<Dp>
) {
    Image(
        painter = painterResource(pizzaImage),
        contentDescription = "Pizza",
        modifier = Modifier
            .padding(top = spacer.value / 2)
            .scale(pizzaScale.value)
            .size(size.value)
    )
}

@Composable
private fun PizzaBoxLid(spacer: State<Dp>, rotation: State<Float>, pizzaScale: State<Float>) {
    Image(
        painter = painterResource(Res.drawable.lid3),
        contentDescription = "Pizza Box Lid",
        modifier = Modifier
            .graphicsLayer { rotationX = rotation.value }
            .scale(pizzaScale.value)
            .padding(bottom = spacer.value / 2)
            .size(BASE_SIZE)
    )
}
