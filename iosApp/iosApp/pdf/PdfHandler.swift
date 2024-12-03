//
//  PdfHandler.swift
//  iosApp
//
//  Created by mac on 22/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import PDFKit
import SwiftUI

@objc public class PDFHelper: NSObject {
    @objc public override init() {
        super.init()
    }

    @objc public func generatePDF(from people: [[String: String]], signature: UIImage?) -> Data? {
        let pageWidth: CGFloat = 612
        let pageHeight: CGFloat = 792
        let margin: CGFloat = 50
        let contentWidth = pageWidth - 2 * margin
        let contentHeight = pageHeight - 2 * margin

        let pdfRenderer = UIGraphicsPDFRenderer(bounds: CGRect(x: 0, y: 0, width: pageWidth, height: pageHeight))
        let rowHeight: CGFloat = 30
        let columnWidths: [CGFloat] = [contentWidth * 0.3, contentWidth * 0.3, contentWidth * 0.4]
        let spacingAboveSignature: CGFloat = 40

        let data = pdfRenderer.pdfData { context in
            context.beginPage()
            var currentY: CGFloat = margin

            // Define attributes for text
            let textAttributes: [NSAttributedString.Key: Any] = [
                .font: UIFont.systemFont(ofSize: 14),
                .paragraphStyle: NSMutableParagraphStyle()
            ]

            // Draw the header row
            let headers = ["First Name", "Last Name", "Email"]
            var currentX: CGFloat = margin
            for (index, header) in headers.enumerated() {
                let columnWidth = columnWidths[index]
                let headerText = NSAttributedString(string: header, attributes: textAttributes)
                headerText.draw(in: CGRect(x: currentX, y: currentY, width: columnWidth, height: rowHeight))
                currentX += columnWidth
            }
            currentY += rowHeight

            // Draw data rows
            for person in people {
                guard let firstname = person["firstname"],
                      let lastname = person["lastname"],
                      let email = person["email"] else { continue }

                let rowValues = [firstname, lastname, email]
                currentX = margin

                for (index, value) in rowValues.enumerated() {
                    let columnWidth = columnWidths[index]
                    let valueText = NSAttributedString(string: value, attributes: textAttributes)
                    valueText.draw(in: CGRect(x: currentX, y: currentY, width: columnWidth, height: rowHeight))
                    currentX += columnWidth
                }
                currentY += rowHeight

                // Add a new page if the content exceeds the available height
                if currentY + rowHeight > contentHeight + margin {
                    context.beginPage()
                    currentY = margin

                    // Redraw the header row on the new page
                    currentX = margin
                    for (index, header) in headers.enumerated() {
                        let columnWidth = columnWidths[index]
                        let headerText = NSAttributedString(string: header, attributes: textAttributes)
                        headerText.draw(in: CGRect(x: currentX, y: currentY, width: columnWidth, height: rowHeight))
                        currentX += columnWidth
                    }
                    currentY += rowHeight
                }
            }

            // Add space above the signature
            currentY += spacingAboveSignature

            // Add "Signature" label and image
            let signatureLabel = "Signature"
            let signatureAttributes: [NSAttributedString.Key: Any] = [
                .font: UIFont.systemFont(ofSize: 14, weight: .medium)
            ]
            let labelWidth = (signatureLabel as NSString).size(withAttributes: signatureAttributes).width
            let gap: CGFloat = 20

            if let signatureImage = signature {
                let scaleFactor: CGFloat = 0.3
                let scaledWidth = signatureImage.size.width * scaleFactor
                let scaledHeight = signatureImage.size.height * scaleFactor
                let totalWidth = labelWidth + gap + scaledWidth

                // Center align label and signature
                let startX = (pageWidth - totalWidth) / 2
                (signatureLabel as NSString).draw(at: CGPoint(x: startX, y: currentY), withAttributes: signatureAttributes)

                let signatureX = startX + labelWidth + gap
                let signatureY = currentY - scaledHeight / 2
                signatureImage.draw(in: CGRect(x: signatureX, y: signatureY, width: scaledWidth, height: scaledHeight))
            }
        }

        return data
    }

    @objc public func savePDF(data: Data, fileName: String) -> URL? {
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
    
    @objc public func byteArrayToUIImage(signature: [UInt8]?) -> UIImage? {
          guard let signatureData = signature else { return nil }

          // Convert ByteArray to NSData
          let nsData = NSData(bytes: signatureData, length: signatureData.count)

          // Create UIImage from NSData
          return UIImage(data: nsData as Data)
      }
}


    
    



