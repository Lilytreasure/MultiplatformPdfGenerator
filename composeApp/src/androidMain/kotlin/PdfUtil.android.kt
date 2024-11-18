import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual object PdfUtil {
    actual fun createAndSavePdf(content: String, fileName: String): String {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint().apply {
            textSize = 16f
            color = android.graphics.Color.BLACK
        }

        // Split the content by lines and render it in a column format
        val lines = content.split("\n")
        val lineHeight = paint.textSize + 8f // Adjust line spacing
        var yPosition = 50f // Start position

        lines.forEach { line ->
            canvas.drawText(line, 10f, yPosition, paint)
            yPosition += lineHeight
        }

        pdfDocument.finishPage(page)

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
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Failed to save PDF: ${e.message}")
        } finally {
            pdfDocument.close()
        }

        return finalFile.absolutePath // Return the path of the saved PDF
    }

}