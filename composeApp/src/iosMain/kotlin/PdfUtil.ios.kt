import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import org.example.project.Decompose.PDFHelper


//Todo ---Render the pd doc using c Interop
actual object PdfUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        context: PlatformContext,
        signature: ByteArray?,
        fileSavedStatus: (url: String) -> Unit
    ): String {

        val pdf2 = PDFHelper()
        val people = listOf(
            mapOf("firstname" to firstname, "lastname" to lastname, "email" to email)
        )
        val pdfData =
            pdf2.generatePDFFrom(people) // Ensure this matches the function in your Swift code
        if (pdfData != null) {
            // Save the PDF
            val savedURL =
                pdf2.savePDFWithData(pdfData, fileName) // Ensure this matches your Swift method
            println("PDF saved at..: $savedURL")
            fileSavedStatus(savedURL.toString())
            return savedURL?.absoluteString ?: "Error: File URL is null"
        } else {
            println("Failed to generate PDF.")
            return "Error: Failed to generate PDF."
        }

     //Sample to show  different ways to handle cinterop in Local directory
//        val pdfHelper = PdfController() // Ensure PdfController is correctly implemented and accessible
//        // Create a list of people
//        val people = listOf(
//            mapOf("firstname" to firstname, "lastname" to lastname, "email" to email)
//        )
//        // Generate the PDF
//        val pdfData = pdfHelper.generatePDFFrom(people) // Ensure this matches the function in your Swift code
//        if (pdfData != null) {
//            // Save the PDF
//            val savedURL = pdfHelper.savePDFWithData(pdfData, fileName) // Ensure this matches your Swift method
//            println("PDF saved at: $savedURL")
//            return savedURL?.absoluteString ?: "Error: File URL is null"
//        } else {
//            println("Failed to generate PDF.")
//            return "Error: Failed to generate PDF."
//        }
    }
}



@Composable
actual fun getPlatformContext(): PlatformContext = IOSPlatformContext