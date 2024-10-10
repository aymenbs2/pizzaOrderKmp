package ui.core

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.champinion
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedCircularImages(
    modifier: Modifier,
    img: DrawableResource=Res.drawable.champinion,
    images: List<DrawableResource> = emptyList(),
    center: Offset = Offset(0f, 0f),
    numberOfImages: Int = 6,
    isAnimated: Boolean = true,
    isSingleImage: Boolean = true,
    radius: Float = 50f
) {
    val isPositioned = remember {
        mutableStateOf(false)
    }
    val animatedRadius =
        if (!isAnimated)
            mutableStateOf(radius)
        else
            animateFloatAsState(
                targetValue = if (isPositioned.value) radius else radius + 40F,
                label = "",
                animationSpec = spring(Spring.DampingRatioHighBouncy)
            )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        val angleStep = 2 * PI / numberOfImages
        for (i in 0 until numberOfImages) {
            val angle = i * angleStep
            val x = center.x + animatedRadius.value * cos(angle).toFloat()
            val y = center.y + animatedRadius.value * sin(angle).toFloat()

            // Draw each image at calculated positions
            Box(
                modifier = Modifier
                    .offset(x.dp, y.dp)
                    .size(30.dp) // Adjust the size as needed
                    .graphicsLayer {
                        translationX = x - center.x
                        translationY = y - center.y
                    }
                    .onGloballyPositioned {
                        isPositioned.value = true
                    }
            ) {
                Image(
                    painter = painterResource(if (isSingleImage) img else images[i]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}
