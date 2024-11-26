package home

import com.arkivanov.decompose.ComponentContext

class DefautHomeComponent(
    componentContext: ComponentContext,
    private val onShowWelcome: () -> Unit,
) : HomeComponent, ComponentContext by componentContext {


    override fun onUpdateGreetingText() {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() {
        TODO("Not yet implemented")
    }
}