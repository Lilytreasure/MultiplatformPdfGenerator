import PdfUtil.createAndSavePdf
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfPreviewView(context: Context, private val content: String) : View(context) {
    private val paint = Paint().apply {
        textSize = 16f
        color = android.graphics.Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 16f
        var yPosition = padding
        val lines = content.split("\n")

        for (line in lines) {
            canvas.drawText(line, padding, yPosition, paint)
            yPosition += paint.textSize + padding // Line height
        }
    }

    /**
     * Generates a PDF file with the given content.
     * @param outputFile The file where the PDF will be saved.
     */
    fun createPdf(outputFile: File) {
        val pdfDocument = PdfDocument()
        val pageWidth = width.coerceAtLeast(595) // Default A4 width in points
        val pageHeight = height.coerceAtLeast(842) // Default A4 height in points

        // Create a new page
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Draw the content on the PDF canvas
        val padding = 16f
        var yPosition = padding
        val lines = content.split("\n")

        for (line in lines) {
            if (yPosition + paint.textSize > pageHeight - padding) {
                // Close current page and create a new one
                pdfDocument.finishPage(page)
                yPosition = padding
                val newPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.pages.size + 1).create()
                val newPage = pdfDocument.startPage(newPageInfo)
                canvas.drawText(line, padding, yPosition, paint)
            } else {
                canvas.drawText(line, padding, yPosition, paint)
                yPosition += paint.textSize + padding
            }
        }

        pdfDocument.finishPage(page)

        // Write the PDF document to the output file
        pdfDocument.writeTo(FileOutputStream(outputFile))
        pdfDocument.close()
    }
}

// Composable function to allow user to type and save a PDF
@Composable
actual fun savePdfDoc(): Launcher {
    val launcherCustom: Launcher?
    val context = LocalContext.current
    launcherCustom = remember {
        Launcher(onLaunch = {pdfretrived->
            println("loaded:::;; file " +pdfretrived.firstname)
            if (pdfretrived.fileName.isBlank()) {
                Toast.makeText(context, "Please enter a valid file name.", Toast.LENGTH_SHORT).show()
                return@Launcher
            }
            try {
                val pdfPath = createAndSavePdf(pdfretrived.firstname, pdfretrived.fileName)
                Toast.makeText(context, "PDF saved to $pdfPath", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    return  launcherCustom
}


