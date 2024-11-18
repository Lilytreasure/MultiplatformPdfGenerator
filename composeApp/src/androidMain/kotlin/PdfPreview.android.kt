import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File
import java.io.FileOutputStream

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
actual fun PdfPreview(content: String) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue(content)) }
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // TextField for User Input (Main Content)
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter Text for PDF") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // TextField for First Name
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // TextField for Last Name
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Combine all the content including first name, last name, and main text
        val combinedContent = buildString {
            append("First Name: ${firstName.text}\n")
            append("Last Name: ${lastName.text}\n")
            append("\n")
            append(text.text)
        }

        // Preview the content in the PdfPreviewView
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context -> PdfPreviewView(context, combinedContent) }
        )

        // Save Button
        Button(
            onClick = {
                val outputDir = context.cacheDir
                val pdfFile = File(outputDir, "preview.pdf")

                // Generate the PDF with combined content
                val pdfPreviewView = PdfPreviewView(context, combinedContent)
                pdfPreviewView.post {
                    pdfPreviewView.createPdf(pdfFile)

                    // Optionally, show a toast message or share the PDF
                    Toast.makeText(context, "PDF saved to ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Save PDF")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewPdfPreview() {
    PdfPreview("This is the initial text.")
}