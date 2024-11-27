import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import rootBottomStack.RootBottomComponent
import rootBottomStack.RootBottomScreen
import theme.ComposeExperimentalTheme

@Composable
@Preview
fun App(component: RootBottomComponent, modifier: Modifier = Modifier) {
    ComposeExperimentalTheme(content = {
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RootBottomScreen(component, modifier)

            }
    })
}