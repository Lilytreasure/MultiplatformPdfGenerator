
import Foundation
import PDFKit
import SwiftUI

@objc public class PdfController: NSObject {
    @objc public override init() {
        super.init()
    }

    @objc public func openPdf(filePathLoc: String) {
        print("Opening PDF at path: \(filePathLoc)")
        // Add logic to handle PDF operations
    }

       @objc func savePDF(data: Data, fileName: String) -> URL? {
            let fileManager = FileManager.default
            guard let documentDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask).first else {
                return nil
            }
            let fileURL = documentDirectory.appendingPathComponent("\(fileName).pdf")

            do {
                try data.write(to: fileURL)
                return fileURL
            } catch {
                print("Error saving PDF: \(error.localizedDescription)")
                return nil
            }
        }
}