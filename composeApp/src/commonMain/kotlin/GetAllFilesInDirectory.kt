import androidx.compose.runtime.Composable


expect fun getAllFilesInDirectory(): List<String>
@Composable
expect fun openPdfDoc(context: PlatformContext): Launcher