import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGContextDrawLayerInRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFView
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsBeginImageContext
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPage
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun savePdfDoc(): Launcher {
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            // Initialize PDFView
            val pdfView = PDFView(CGRectMake(0.0, 0.0, 0.0, 0.0))

            // Create a PDFDocument
            val pdfDocument = PDFDocument()

            // Generate a UIImage from the text content
            val image = createImageFromText(firstname)

            // Create a PDFPage with the generated UIImage
            if (image != null) {
                val tempFile = NSTemporaryDirectory() + "temp.pdf"
                UIGraphicsBeginPDFContextToFile(tempFile, CGRectZero.readValue(), null)
                UIGraphicsBeginPDFPage()
                image.drawInRect(CGRectMake(0.0, 0.0, 100.0, 200.0))
                UIGraphicsEndPDFContext()

                val tempPdfDocument = PDFDocument(NSData.fileURLWithPath(tempFile))
                tempPdfDocument?.let { tempDoc ->
                    for (i in 0 until tempDoc.pageCount) {
                        pdfDocument.insertPage(tempDoc.pageAtIndex(i)!!, pdfDocument.pageCount)
                    }
                }
            }

            pdfView.document = pdfDocument
            pdfView.autoScales = true
            pdfView
        }
    )
}

// Helper function to generate a UIImage from text content
@OptIn(ExperimentalForeignApi::class)
private fun createImageFromText(content: String): UIImage? {
    val canvasSize = CGSizeMake(300.0, 600.0) // Define the canvas size
    UIGraphicsBeginImageContext(canvasSize)
    val context = UIGraphicsGetCurrentContext() ?: return null
    // Draw a white background
    UIColor.whiteColor().setFill()
    context.fillRect(CGRectMake(0.0, 0.0, 200.0, 100.0))

    // Draw the text content
    UIColor.blackColor().set()
    val textRect = CGRectMake(20.0, 20.0, canvasSize.size - 40.0, canvasSize.size - 40.0)
    CGContextDrawLayerInRect(textRect  withattributes = mapOf(
        NSAttributedString.Key.font to UIFont.systemFontOfSize(16.0)
    ))
    // Retrieve the generated UIImage
    val image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image
}