import androidx.compose.ui.unit.Dp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getPagerTopPadding(): Dp
expect fun getPagerStartPadding(): Dp