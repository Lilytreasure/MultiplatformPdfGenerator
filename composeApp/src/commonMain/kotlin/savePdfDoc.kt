import androidx.compose.runtime.Composable


//Todo----- Work on file upload and  download on Android/iOs
data class PdfDocData(
    var firstname: String,
    var lastname: String,
    var email: String,
    var fileName: String
)

@Composable
expect fun savePdfDoc(fileLocation: (url: String) -> Unit): Launcher



