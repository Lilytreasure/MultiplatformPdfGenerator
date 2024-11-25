
import Foundation

@objc public class PdfController: NSObject {
    @objc public override init() {
        super.init()
    }

    @objc public func openPdf(filePath: String) {
        print("Opening PDF at path: \(filePath)")
        // Add logic to handle PDF operations
    }
}