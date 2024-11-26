package receipt

import PlatformSpecific
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class DefaultReceiptComponent(
    componentContext: ComponentContext,
    private val onShowWelcome: () -> Unit,
) : ReceiptComponent, ComponentContext by componentContext, KoinComponent {
    override val platformSpecific: PlatformSpecific by inject()

    override fun onUpdateGreetingText() {
        TODO("Not yet implemented")
    }
    override fun onBackClicked() {
        TODO("Not yet implemented")
    }

}