package receipt

import PlatformSpecific


interface ReceiptComponent {
    val platformSpecific: PlatformSpecific
    fun onUpdateGreetingText()
    fun onBackClicked()
}