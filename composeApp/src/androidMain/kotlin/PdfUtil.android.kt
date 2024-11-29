import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import org.example.project.R
import java.io.File
import java.io.IOException

class AndroidPlatformContext(val context: Context) : PlatformContext

actual object PdfUtil {
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        context: PlatformContext,
        fileSavedStatus: (url: String) -> Unit
    ): String {
        val androidContext = (context as AndroidPlatformContext).context
        val currentActivity: AppCompatActivity = (androidContext as AppCompatActivity)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }

        // Define text dimensions and positions
        val startX = 50f
        val startY = 50f
        val columnWidth = 150f
        val rowHeight = 50f

        // Add header row
        paint.textSize = 16f
        paint.textAlign = Paint.Align.CENTER
        val headers = listOf("First Name", "Last Name", "Email")
        headers.forEachIndexed { index, header ->
            val textX = startX + index * columnWidth + columnWidth / 2
            val textY = startY + rowHeight / 2 - (paint.descent() + paint.ascent()) / 2
            canvas.drawText(header, textX, textY, paint)
        }

        // Add user data row
        val userData = listOf(firstname, lastname, email)
        userData.forEachIndexed { index, data ->
            val textX = startX + index * columnWidth + columnWidth / 2
            val textY = startY + rowHeight + rowHeight / 2 - (paint.descent() + paint.ascent()) / 2
            canvas.drawText(data, textX, textY, paint)
        }
        pdfDocument.finishPage(page)

        // Saving the PDF using Scoped Storage
        val fileUri: Uri = savePdfToMediaStore(currentActivity, pdfDocument, fileName)

        // Notify the result
        fileSavedStatus(fileUri.toString()) // Return the URI of the saved file

        pdfDocument.close()
        return fileUri.toString() // Return the URI as the file path
    }
}

private fun savePdfToMediaStore(
    context: Context,
    pdfDocument: PdfDocument,
    fileName: String
): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.pdf")
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Pdfgen") // Scoped Storage path
    }

    // Insert into MediaStore
    val uri = context.contentResolver.insert(
        MediaStore.Files.getContentUri("external"),
        contentValues
    ) ?: throw IOException("Failed to create file in MediaStore")

    // Open the output stream to the URI
    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
        pdfDocument.writeTo(outputStream) // Write the PDF content to the URI
    } ?: throw IOException("Failed to open output stream for saving PDF")
    showPdfSaveCompleteNotification(context, fileName, uri)

    return uri
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun saveFileUsingMediaStore(
    activity: Activity,
    sourceFile: File,
    fileName: String
) {
    val resolver = activity.contentResolver
    val directoryPath = Environment.DIRECTORY_DOWNLOADS + "/Pdfgen"
    var uniqueFileName = fileName
    var fileUri: Uri? = null
    var increment = 1

    while (fileUri == null) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, uniqueFileName)
            put(MediaStore.MediaColumns.RELATIVE_PATH, directoryPath)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        }

        fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (fileUri == null) {
            // If the file with the same name exists, modify the file name to make it unique
            val baseName = fileName.substringBeforeLast(".")
            val extension = fileName.substringAfterLast(".", "")
            uniqueFileName = if (extension.isEmpty()) {
                "$baseName ($increment)"
            } else {
                "$baseName ($increment).$extension"
            }
            increment++
        }
    }

    try {
        // Open the output stream for the URI and copy data from the source file
        resolver.openOutputStream(fileUri)?.use { outputStream ->
            sourceFile.inputStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        // Show file saved successfully
        showPdfSaveCompleteNotification(activity, uniqueFileName, fileUri)
    } catch (e: IOException) {
        Log.e("SaveFile", "Error saving file: ${e.message}")
        // Toast.makeText(activity, "Error saving file: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private fun showPdfSaveCompleteNotification(context: Context, fileName: String, fileUri: Uri) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "download_complete_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Download Complete",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
    val notificationIntent = Intent(Intent.ACTION_VIEW)
    notificationIntent.setDataAndType(fileUri, "application/pdf")
    notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        notificationIntent,
        flags
    )
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Download Complete")
        .setContentText("File: $fileName")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
    notificationManager.notify(0, notificationBuilder.build())
}

