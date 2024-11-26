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
    @objc public func generatePDF(from people: [[String: String]]) -> Data? {
        let pageWidth: CGFloat = 612
        let pageHeight: CGFloat = 792
        let margin: CGFloat = 50
        let contentWidth = pageWidth - 2 * margin
        let contentHeight = pageHeight - 2 * margin
        
        let pdfRenderer = UIGraphicsPDFRenderer(bounds: CGRect(x: 0, y: 0, width: pageWidth, height: pageHeight))
        
        var currentY: CGFloat = margin
        let textAttributes: [NSAttributedString.Key: Any] = [
            .font: UIFont.systemFont(ofSize: 14),
            .paragraphStyle: NSMutableParagraphStyle()
        ]
        
        let data = pdfRenderer.pdfData { context in
            context.beginPage()
            
            for person in people {
                guard let firstname = person["firstname"],
                      let lastname = person["lastname"],
                      let email = person["email"] else { continue }
                
                let personContent = """
                First Name: \(firstname)
                Last Name: \(lastname)
                Email: \(email)
                """
                
                let attributedString = NSAttributedString(string: personContent, attributes: textAttributes)
                let textHeight = attributedString.boundingRect(with: CGSize(width: contentWidth, height: .greatestFiniteMagnitude), options: .usesLineFragmentOrigin, context: nil).height + 30
                
                if currentY + textHeight > contentHeight + margin {
                    context.beginPage()
                    currentY = margin
                }
                
                attributedString.draw(in: CGRect(x: margin, y: currentY, width: contentWidth, height: textHeight))
                currentY += textHeight
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
}


    
    



