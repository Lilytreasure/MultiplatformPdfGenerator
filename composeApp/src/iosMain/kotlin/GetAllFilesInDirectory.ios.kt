import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDirectoryEnumerationIncludesDirectoriesPostOrder
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.lastPathComponent


@OptIn(ExperimentalForeignApi::class)
actual fun getAllFilesInDirectory(): List<String> {
    val fileManager = NSFileManager.defaultManager
    val documentsPath = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,
        NSUserDomainMask,
        true
    ).firstOrNull() as? String ?: return emptyList()

    val directoryPath = "$documentsPath/Pdfgen"
    val url = NSURL.fileURLWithPath(directoryPath)

    val contents = fileManager.contentsOfDirectoryAtURL(
        url,
        includingPropertiesForKeys = null,
        options = NSDirectoryEnumerationIncludesDirectoriesPostOrder,
        error = null
    )?.filterIsInstance<NSURL>()

    return contents?.map { it.lastPathComponent ?: "" } ?: emptyList()
}

//Todo--Open the clicked  docs
@Composable
actual fun openPdfDoc(context: PlatformContext): Launcher {
    val launcherCustom: Launcher?

    launcherCustom = remember {
        Launcher(onLaunch = { _, filename ->


        })
    }

    return launcherCustom
}