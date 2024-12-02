import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import java.io.File

actual fun getAllFilesInDirectory(context: PlatformContext): List<String> {
    val androidContext = (context as AndroidPlatformContext).context

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getFilesFromMediaStore(androidContext)
    } else {
        getFilesFromLegacyStorage()
    }
}

//Read files from Android 10 and above
private fun getFilesFromMediaStore(context: Context): List<String> {
    val files = mutableListOf<String>()
    val contentResolver: ContentResolver = context.contentResolver
    val uri: Uri = MediaStore.Files.getContentUri("external")
    val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)

    val cursor: Cursor? = contentResolver.query(
        uri,
        projection,
        "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?",
        arrayOf("%Pdfgen%"),
        null
    )

    cursor?.use {
        val displayNameColumn = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
        while (it.moveToNext()) {
            val fileName = it.getString(displayNameColumn)
            files.add(fileName)
        }
    }

    return files
}
//Read files below Android 10
private fun getFilesFromLegacyStorage(): List<String> {
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