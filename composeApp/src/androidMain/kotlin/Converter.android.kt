import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * Converts [ImageBitmap] to image with desired [format] and returns its bytes.
 *
 * */

actual typealias ImageFormat = Bitmap.CompressFormat
actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
    return ByteArrayOutputStream().use {
        asAndroidBitmap().compress(format, 100, it)
        it.toByteArray()
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    // Decode the ByteArray into a Bitmap
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    // Convert the Bitmap to ImageBitmap
    return bitmap.asImageBitmap()
}