import SwiftUI
import ComposeApp


@main
struct iOSApp: App {
    private var lifecycle = LifecycleRegistryKt.LifecycleRegistry()
    
    var body: some Scene {
        WindowGroup {
            GeometryReader { geo in
                ContentView(
                    lifecycle:lifecycle
                )
                .onTapGesture {
                    // Hide keyboard on tap outside of TextField
                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                }
            }
            
        }
    }
}
