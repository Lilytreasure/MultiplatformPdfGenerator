import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import java.io.File

actual fun getAllFilesInDirectory(): List<String> {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "Pdfgen"
    )
    return if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.map { it.name } ?: emptyList()
    } else {
        emptyList()
    }
}
@Composable
actual fun openPdfDoc(context: PlatformContext) : Launcher {
    val launcherCustom: Launcher?
    val androidContext = (context as AndroidPlatformContext).context
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "Pdfgen"
    )
    launcherCustom = remember {
        Launcher(onLaunch = { _, filename->
            val file = filename?.let { File(directory, it) }
            if (file != null) {
                if (file.exists()) {
                    val uri: Uri = FileProvider.getUriForFile(
                        androidContext,
                        "${androidContext.packageName}.fileprovider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    androidContext.startActivity(Intent.createChooser(intent, "Open PDF"))
                } else {
                    Toast.makeText(androidContext, "File not found: $filename", Toast.LENGTH_LONG).show()
                }
            }

        })
    }
    return launcherCustom
}