import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import platform.UIKit.UIViewController
import rootBottomStack.DefaultRootBottomComponent
import theme.ComposeExperimentalTheme

@Suppress("unused", "FunctionName")
fun MainViewController(
    lifecycle: LifecycleRegistry,
): UIViewController {
    val defaultComponentCtx = DefaultComponentContext(lifecycle = lifecycle)
    val root = DefaultRootBottomComponent(
        componentContext = defaultComponentCtx
    )
    initKoin(enableNetworkLogs = true, platform = PlatformSpecific())
    return ComposeUIViewController {
        ComposeExperimentalTheme {
            App(root)
        }
    }
}