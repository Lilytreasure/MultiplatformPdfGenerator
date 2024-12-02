package pdfView

import PlatformSpecific
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class DefaultPdfViewComponent(
    componentContext: ComponentContext,
    private val onShowWelcome: () -> Unit,
) : PdfViewComponent, ComponentContext by componentContext, KoinComponent {
    override val platformSpecific: PlatformSpecific by inject()

    override fun onUpdateGreetingText() {
        TODO("Not yet implemented")
    }
    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

}