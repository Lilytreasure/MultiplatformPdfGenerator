expect class Launcher(
    onLaunch: (pdfDocData: PdfDocData) -> Unit,
) {
    fun launch(pdfDocData: PdfDocData)
}