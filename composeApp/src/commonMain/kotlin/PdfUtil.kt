import androidx.compose.runtime.Composable

interface PlatformContext
@Composable
expect fun getPlatformContext(): PlatformContext

expect object PdfUtil {
    fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        context: PlatformContext,
        signature: ByteArray?,
        fileSavedStatus:(url: String)->Unit
    ): String
}