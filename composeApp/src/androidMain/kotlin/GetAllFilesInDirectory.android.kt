import android.os.Environment
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