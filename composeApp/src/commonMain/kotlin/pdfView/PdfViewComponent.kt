package pdfView

import PlatformSpecific


interface PdfViewComponent {
    val platformSpecific: PlatformSpecific
    fun onUpdateGreetingText()
    fun onBackClicked()
}