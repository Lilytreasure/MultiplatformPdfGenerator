import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import org.example.project.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AndroidPlatformContext(val context: Context) : PlatformContext

actual object PdfUtil {
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        context: PlatformContext,
        signature: ByteArray?,
        fileSavedStatus: (url: String) -> Unit
    ): String {
        val androidContext = (context as AndroidPlatformContext).context
        val currentActivity: AppCompatActivity = (androidContext as AppCompatActivity)
        // Create the PDF document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()  // A4 size (595x842)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint().apply { isAntiAlias = true }
        // Define text dimensions and positions
        // Canvas will give you the freedom to play with constraints to design the pdf as you wish
        //For more Versatile  form designs you could use itext
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
        // Add space above the signature section
        val signatureMargin = 80f // Space between user data and signature section
        val signatureStartY = startY + rowHeight * 2 + signatureMargin

        // Center-align signature text and signature on the same row
        val pageWidth = pageInfo.pageWidth.toFloat()
        val title = "Signature"
        paint.textSize = 14f
        paint.textAlign = Paint.Align.LEFT
        val titleWidth = paint.measureText(title)

        // Add signature next to the title
        signature?.let {
            // Decode the signature ByteArray to Bitmap
            val signatureBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

            // Check if the bitmap is valid
            signatureBitmap?.let { bitmap ->
                // Scale the signature bitmap
                val scaleFactor = 0.3f
                val scaledWidth = (bitmap.width * scaleFactor).toInt()
                val scaledHeight = (bitmap.height * scaleFactor).toInt()
                val scaledBitmap =
                    Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)

                // Calculate total width (text + gap + signature)
                val gap = 20f // Space between the text and the signature
                val totalWidth = titleWidth + gap + scaledWidth

                // Calculate the starting X position to center-align
                val startX2 = (pageWidth - totalWidth) / 2

                // Center-align the text
                canvas.drawText(title, startX2, signatureStartY, paint)

                // Center-align the signature next to the text
                val signatureStartX = startX2 + titleWidth + gap
                val signatureBitmapStartY =
                    signatureStartY - scaledHeight / 2 // Adjust to align vertically with text
                canvas.drawBitmap(scaledBitmap, signatureStartX, signatureBitmapStartY, paint)
            }
        }
        // Finish the page and document
        pdfDocument.finishPage(page)
        val fileUri: Uri? = savePdfToStorage(currentActivity, pdfDocument, fileName)

        fileSavedStatus(fileUri.toString()) // Return the URI of the saved file

        pdfDocument.close()
        return fileUri.toString()
    }
}

private fun savePdfToStorage(
    context: Context,
    pdfDocument: PdfDocument,
    fileName: String
): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10 (API 29) and above - Scoped Storage (MediaStore)
        savePdfToMediaStore(context, pdfDocument, fileName)
    } else {
        // For Android versions below 10, save directly to the storage
        savePdfToLegacyStorage(context, pdfDocument, fileName)
        null
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
        put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            Environment.DIRECTORY_DOCUMENTS + "/Pdfgen"
        ) // Scoped Storage path
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

private fun savePdfToLegacyStorage(
    context: Context,
    pdfDocument: PdfDocument,
    fileName: String
) {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "Pdfgen"
    )
    // Ensure the directory exists
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val file = File(directory, "$fileName.pdf")
    try {
        FileOutputStream(file).use { output ->
            pdfDocument.writeTo(output) // Write PDF to the file
        }
        Toast.makeText(context, "File saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()

        // For legacy storage (below Android 10), show notification without URI
        showPdfSaveCompleteNotification(context, fileName, Uri.fromFile(file))
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private fun showPdfSaveCompleteNotification(context: Context, fileName: String, fileUri: Uri) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "download_complete_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Pdf Saved Successfully",
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
        .setContentTitle("Pdf Saved Successfully")
        .setContentText("File: $fileName")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
    notificationManager.notify(0, notificationBuilder.build())
}

@Composable
actual fun getPlatformContext(): PlatformContext {
    val context = LocalContext.current
    return AndroidPlatformContext(context)
}