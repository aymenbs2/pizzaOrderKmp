import android.os.Build
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual val PIZZA_PAGER_HORIZONTAL_PADDING =80.dp
actual val PIZZA_ANIM_EDGE_SIZE_COEF =1.6f
actual val PIZZA_PLATE_PADDING = 55
actual val PIZZA_PAGER_PADDING = 15
