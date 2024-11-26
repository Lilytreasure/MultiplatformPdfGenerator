package rootBottomStack


import home.HomeComponent
import home.DefautHomeComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import kotlinx.serialization.Serializable
import receipt.DefaultReceiptComponent
import receipt.ReceiptComponent


class DefaultRootBottomComponent(
    componentContext: ComponentContext

) : RootBottomComponent, ComponentContext by componentContext {
    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            serializer = ConfigBottom.serializer(),
            initialConfiguration = ConfigBottom.Feeds,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "authStack"
        )

    override val childStackBottom: Value<ChildStack<*, RootBottomComponent.ChildBottom>> =
        _childStackBottom


    private fun createChildBottom(
        config: ConfigBottom,
        componentContext: ComponentContext
    ): RootBottomComponent.ChildBottom =
        when (config) {

            is ConfigBottom.Feeds -> RootBottomComponent.ChildBottom.FeedsChild(
                feedsComponent(componentContext)
            )

            is ConfigBottom.Notification -> RootBottomComponent.ChildBottom.NotificationsChild(
                notificationComponent(componentContext)
            )
        }

    private fun feedsComponent(componentContext: ComponentContext): HomeComponent =
        DefautHomeComponent(
            componentContext = componentContext,
            onShowWelcome = {

            }

        )

    private fun notificationComponent(componentContext: ComponentContext): ReceiptComponent =
        DefaultReceiptComponent(
            componentContext = componentContext,
            onShowWelcome = {

            }

        )


    override fun openFeeds() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Feeds)
    }

    override fun openNotifications() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Notification)
    }

    @Serializable
    private sealed class ConfigBottom {

        @Serializable
        data object Feeds : ConfigBottom()

        @Serializable
        data object Notification : ConfigBottom()

    }

    init {
        lifecycle.subscribe(object : Lifecycle.Callbacks {
            override fun onResume() {
                when (childStackBottom.active.configuration) {
                    is ConfigBottom.Notification -> {
                        super.onResume()
                    }

                }
            }
        })

    }

}