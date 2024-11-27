import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    
    private let lifecycle: LifecycleRegistry

    init(lifecycle: LifecycleRegistry) {
        self.lifecycle = lifecycle
    }
    
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            lifecycle: lifecycle
        )

    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
   

}

struct ContentView: View {
    private let lifecycle: LifecycleRegistry

    
    init(lifecycle: LifecycleRegistry) {
        self.lifecycle = lifecycle
      
    }
    var body: some View {
        ComposeView( lifecycle: lifecycle)
            .ignoresSafeArea(.all, edges: [.top, .bottom]) // Compose has own keyboard handler
    }
}






