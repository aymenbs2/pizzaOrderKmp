import android.os.Build
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual fun getPagerTopPadding(): Dp = 22.4.dp

actual fun getPagerStartPadding(): Dp = 4.dp