import PdfUtil.createAndSavePdf
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.border
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
actual fun PdfPreview(content: String) {
    val context = LocalContext.current
    // States for user input
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("") }

    // Combine content for preview
    val combinedContent = remember(firstName, lastName, email) {
        buildString {
            append("First Name: $firstName\n")
            append("Last Name: $lastName\n")
            append("Email: $email\n")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TextFields for user input
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("File Name (without .pdf)") },
            modifier = Modifier.fillMaxWidth()
        )

//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp)
//                .border(1.dp, MaterialTheme.colorScheme.onBackground),
//            factory = { context -> PdfPreviewView(context, combinedContent) }
//        )

        // Save PDF Button
        Button(
            onClick = {
                if (fileName.isBlank()) {
                    Toast.makeText(context, "Please enter a valid file name.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                try {
                    val pdfPath = createAndSavePdf(combinedContent, fileName)
                    Toast.makeText(context, "PDF saved to $pdfPath", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save PDF d")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewPdfPreview() {
    PdfPreview("This is the initial text.")
}