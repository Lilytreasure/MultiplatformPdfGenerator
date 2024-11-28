import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual object PdfUtil {
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        fileSavedStatus: (url: String) -> Unit
    ): String {
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
        // Set the directory to save the PDF file
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        // Ensure the directory exists
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$fileName.pdf")

        // Handle existing file by renaming or overwriting
        val finalFile = if (file.exists()) {
            File(directory, "${fileName}_${System.currentTimeMillis()}.pdf") // Add timestamp to avoid conflict
        } else {
            file
        }
        try {
            FileOutputStream(finalFile).use { output ->
                pdfDocument.writeTo(output)
            }

            fileSavedStatus(finalFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to save PDF: ${e.message}")
        } finally {
            pdfDocument.close()
        }

        return finalFile.absolutePath // Return the path of the saved PDF
    }
}