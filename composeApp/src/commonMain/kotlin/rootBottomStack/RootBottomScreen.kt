package rootBottomStack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import home.HomeContent
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import receipt.NotificationContent

data class ScreensBottom(val name: String, val openScreen: () -> Unit, val isSelected: Boolean)

@Composable
fun RootBottomScreen(component: RootBottomComponent, modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val screens by remember {
        mutableStateOf(
            listOf(
                ScreensBottom("Home", component::openFeeds, false),
                ScreensBottom("Receipt", component::openNotifications, false)
            )
        )
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    screens.forEachIndexed { index, screensBottom ->
                        NavigationBarItem(
                            icon = {
                                when (screensBottom.name) {
                                    "Home" -> Icon(
                                        Icons.Outlined.PictureAsPdf,
                                        contentDescription = null
                                    )
                                    "Receipt" -> Icon(
                                        Icons.Outlined.ReceiptLong,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = screensBottom.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Light
                                )
                            },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                screensBottom.openScreen()
                            },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            )

        },
        content = { innerpadding ->
            Column(modifier = Modifier.padding(innerpadding)) {
                Children(
                    stack = component.childStackBottom,
                    modifier = modifier,
                    animation = stackAnimation(fade() + scale()),
                ) {
                    when (val child = it.instance) {
                        is RootBottomComponent.ChildBottom.FeedsChild -> HomeContent(component = child.component)
                        is RootBottomComponent.ChildBottom.NotificationsChild -> NotificationContent(
                            component = child.component
                        )
                    }
                }
            }
        })


}