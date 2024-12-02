expect class Launcher(
    onLaunch: (pdfDocData: PdfDocData?,filename: String?) -> Unit,
) {
    fun launch(pdfDocData: PdfDocData?=null,filename: String?=null)
}