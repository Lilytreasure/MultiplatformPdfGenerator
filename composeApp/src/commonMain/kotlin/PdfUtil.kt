
interface PlatformContext

expect object PdfUtil {
    fun createAndSavePdf(
        firstname: String,
        lastname: String,
        email: String,
        fileName: String,
        context: PlatformContext,
        fileSavedStatus:(url: String)->Unit
    ): String
}