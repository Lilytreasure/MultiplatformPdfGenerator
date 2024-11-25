actual class Launcher actual constructor(
    private val onLaunch: (pdfDocData: PdfDocData) -> Unit,
) {
    actual fun launch(pdfDocData: PdfDocData) {
        onLaunch(pdfDocData)
    }
}