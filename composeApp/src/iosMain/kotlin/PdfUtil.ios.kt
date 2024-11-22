import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.*
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFPage
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.drawAtPoint


//Todo ---Render the pd doc using c Interop
actual object PdfUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String
    ): String {
        val pageWidth = 595.0
        val pageHeight = 842.0
        val pdfDocument = PDFDocument()
        val page = PDFPage()

        // Table drawing setup
        val startX = 50.0
        val startY = 50.0
        val columnWidth = 150.0
        val rowHeight = 50.0
        val numRows = 4
        val numColumns = 3

        val context = UIGraphicsBeginImageContextWithOptions(CGSizeMake(pageWidth, pageHeight), false, 0.0)!!
        val graphicsContext = UIGraphicsGetCurrentContext()!!

        graphicsContext.saveGState()

        // Draw table rows
        for (row in 0..numRows) {
            val top = startY + row * rowHeight
            graphicsContext.moveToPoint(startX, top)
            graphicsContext.addLineToPoint(startX + numColumns * columnWidth, top)
        }
        // Draw table columns
        for (col in 0..numColumns) {
            val left = startX + col * columnWidth
            graphicsContext.moveToPoint(left, startY)
            graphicsContext.addLineToPoint(left, startY + numRows * rowHeight)
        }
        graphicsContext.strokePath()

        // Draw header row
        val headers = listOf("First Name", "Last Name", "Email")
        headers.forEachIndexed { index, header ->
            val textX = startX + index * columnWidth + columnWidth / 2
            val textY = startY + rowHeight / 2
            val attrString = NSAttributedString(
                string = header,
                attributes = mapOf(NSFontAttributeName to UIFont.systemFontOfSize(16.0))
            )
            attrString.drawAtPoint(CGPointMake(textX, textY))
        }

        graphicsContext.restoreGState()

        UIGraphicsEndImageContext()

        // Save the PDF
        val fileManager = NSFileManager.defaultManager()
        val documentsDir = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        ).first() as String
        val filePath = "$documentsDir/$fileName.pdf"

        val url = NSURL.fileURLWithPath(filePath)
        pdfDocument.writeToFile(url.toString())
        return filePath
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun String.drawAtPoint(point: CValue<CGPoint>, context: CGContextRef?) {
    this as NSString
    this.drawAtPoint(point,  context)
}