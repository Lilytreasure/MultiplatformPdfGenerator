import androidx.compose.runtime.Composable
data class PdfDocData(
    var firstname: String,
    var lastname: String,
    var email: String,
    var fileName: String
)

@Composable
expect fun savePdfDoc(): Launcher



