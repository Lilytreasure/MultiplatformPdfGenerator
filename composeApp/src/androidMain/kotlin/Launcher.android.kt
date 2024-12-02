actual class Launcher actual constructor(
    private val onLaunch: (pdfDocData: PdfDocData?, filename: String?) -> Unit
) {
    actual fun launch(pdfDocData: PdfDocData?,filename: String?) {
        onLaunch(pdfDocData,filename)
    }
}