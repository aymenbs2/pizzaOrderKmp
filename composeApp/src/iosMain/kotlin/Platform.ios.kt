import androidx.compose.ui.unit.dp
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val PIZZA_PAGER_HORIZONTAL_PADDING = 95.dp
actual val PIZZA_ANIM_EDGE_SIZE_COEF = 1.5f
actual val PIZZA_PLATE_PADDING = 50
actual val PIZZA_PAGER_PADDING = 0
