package rootBottomStack

import home.HomeComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import pdfView.PdfViewComponent
import receipt.ReceiptComponent


interface RootBottomComponent {
    val childStackBottom: Value<ChildStack<*, ChildBottom>>
    fun openFeeds()
    fun openNotifications()

    fun openPdfView()

    sealed class ChildBottom {
        class FeedsChild(val component: HomeComponent) : ChildBottom()
        class NotificationsChild(val component: ReceiptComponent) : ChildBottom()
        class PdfViewChild(val component: PdfViewComponent) : ChildBottom()
    }


}