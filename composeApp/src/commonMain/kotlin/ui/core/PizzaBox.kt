package ui.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned

import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.base3
import pizzaoderkmp.composeapp.generated.resources.closed
import pizzaoderkmp.composeapp.generated.resources.lid3
import pizzaoderkmp.composeapp.generated.resources.opened


@Composable
fun PizzaBox(
    modifier: Modifier,
    pizzaImage: DrawableResource,
    isVisible: MutableState<Boolean>,
    isClosed: MutableState<Boolean>,
    onAnimationFinish: () -> Unit
) {
    val duration = 2000
    val isGloballyPositionned = remember {
        mutableStateOf(false)
    }

    val spacer = animateDpAsState(
        targetValue = if (isClosed.value) 0.dp else 250.dp,
        label = "",
        animationSpec = tween(duration),
    )


    val size = animateDpAsState(
        targetValue = if (isClosed.value) 0.dp else 230.dp,
        label = "",
        animationSpec = tween(duration)
    )
    val offset = animateDpAsState(
        targetValue = if (isClosed.value) 150.dp else 0.dp,
        label = "",
        animationSpec = tween(duration, delayMillis = 90),

        )
    val pizzaScale = animateFloatAsState(
        targetValue = if (isClosed.value && isVisible.value) 0f else 1f,
        label = "",
        animationSpec = tween(duration, delayMillis = 90),
        finishedListener = {
            if (it == 0f) {
                onAnimationFinish()
            }

            isClosed.value = false
            isVisible.value = false

        }
    )
    val boxRotation by animateFloatAsState(
        targetValue = if (!isClosed.value) 0f else 90f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 50f),
        label = ""
    )

    val rotation = animateFloatAsState(
        targetValue = if (isClosed.value) 0f else 26F,
        label = "",
        animationSpec = tween(duration),
    )
    LaunchedEffect(isGloballyPositionned.value) {

        delay(1000)
        if (isGloballyPositionned.value){
            isClosed.value = true
        }

    }
    val scale by animateFloatAsState(
        targetValue = if (!isClosed.value) 1f else 0.8f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    AnimatedVisibility(modifier = modifier.onGloballyPositioned {
        isGloballyPositionned.value = true

    }, enter = fadeIn(), exit = fadeOut(), visible = isVisible.value) {


        Box(
            modifier = Modifier
                .alpha(pizzaScale.value)
                .offset(x = offset.value, y = -offset.value)
                .fillMaxSize()
        ) {
            // Pizza box base
            Image(
                painter = painterResource(Res.drawable.base3),
                contentDescription = "Pizza Box Base",
                modifier = Modifier
                    .padding(top = spacer.value / 2)
                    .size(300.dp)
                    .graphicsLayer {
                        rotationX = rotation.value
                    }
                    .scale(pizzaScale.value)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(pizzaImage),
                contentDescription = "Pizza ",
                modifier = Modifier
                    .padding(top = spacer.value / 2)
                    .scale(pizzaScale.value)
                    .size(size.value)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource( Res.drawable.lid3),
                contentDescription = "Pizza Box Lid",
                modifier = Modifier
                    .graphicsLayer {
                        rotationX = rotation.value
                    }
                    .scale(pizzaScale.value)
                    .padding(bottom = spacer.value / 2)
                    .size(300.dp)
                    .align(Alignment.Center)

            )
        }
    }
}


@Composable
fun PizzaBoxAnimation() {
    var isOpen by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isOpen) 0f else 90f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 50f)
    )
    val scale by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0.8f,
        animationSpec = tween(durationMillis = 500)
    )
    val pizzaOffsetY by animateFloatAsState(
        targetValue = if (isOpen) 0f else -100f,
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .graphicsLayer {
                    rotationZ = rotation
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            Image(
                painter = painterResource( if (isOpen) Res.drawable.opened else Res.drawable.closed),
                contentDescription = if (isOpen) "Boîte de pizza ouverte" else "Boîte de pizza fermée",
                modifier = Modifier.fillMaxSize()
            )
        }

        Button(
            onClick = { isOpen = !isOpen },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = if (isOpen) "Fermer la boîte" else "Ouvrir la boîte")
        }
    }
}