actual class Launcher actual constructor(
    private val onLaunch: () -> Unit,
) {
    actual fun launch() {
        onLaunch()
    }
}