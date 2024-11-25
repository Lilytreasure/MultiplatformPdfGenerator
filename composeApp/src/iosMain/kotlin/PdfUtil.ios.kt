import kotlinx.cinterop.ExperimentalForeignApi




//Todo ---Render the pd doc using c Interop
actual object PdfUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String
    ): String {
        val pdf=KCrypto()


        return ""
    }
}

