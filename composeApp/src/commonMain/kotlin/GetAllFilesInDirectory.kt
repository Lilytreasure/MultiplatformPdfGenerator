import androidx.compose.runtime.Composable


expect fun getAllFilesInDirectory(context: PlatformContext): List<String>
@Composable
expect fun openPdfDoc(context: PlatformContext): Launcher