
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image


/**
 * Converts [ImageBitmap] to image with desired [format] and returns its bytes.
 * */

actual typealias ImageFormat = EncodedImageFormat
actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
    val data = Image
        .makeFromBitmap(asSkiaBitmap())
        .encodeToData(format) ?: error("This painter cannot be encoded to $format")

    return data.bytes
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    val image = Image.makeFromEncoded(this)
    return image.toComposeImageBitmap()
}