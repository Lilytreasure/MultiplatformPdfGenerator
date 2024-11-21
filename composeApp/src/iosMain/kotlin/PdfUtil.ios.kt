import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.*
import platform.UIKit.UIGraphicsBeginPDFContextToFile
import platform.UIKit.UIGraphicsBeginPDFPage
import platform.UIKit.UIGraphicsEndPDFContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.drawAtPoint

actual object PdfUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createAndSavePdf(firstname: String, lastname: String, email: String, fileName: String): String {
        val documentsPath = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() ?: throw IllegalStateException("Documents directory not found")

        val filePath = "$documentsPath/$fileName.pdf"

        UIGraphicsBeginPDFContextToFile(filePath, CGRectZero.readValue(), null)
        val context = UIGraphicsGetCurrentContext()

        UIGraphicsBeginPDFPage()
        // Draw text content (firstname, lastname, email)
        val text = "First Name: $firstname\nLast Name: $lastname\nEmail: $email"
        text.drawAtPoint(CGPointMake(10.0, 10.0), context)

        UIGraphicsEndPDFContext()
        return filePath // Return the path of the saved PDF
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun String.drawAtPoint(point: CValue<CGPoint>, context: CGContextRef?) {
    this as NSString
    this.drawAtPoint(point,  context)
}