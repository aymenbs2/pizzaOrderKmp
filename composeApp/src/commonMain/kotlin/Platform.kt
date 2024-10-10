import androidx.compose.ui.unit.Dp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val PIZZA_PAGER_HORIZONTAL_PADDING: Dp

expect val PIZZA_ANIM_EDGE_SIZE_COEF: Float

expect val PIZZA_PLATE_PADDING: Int
expect val PIZZA_PAGER_PADDING: Int