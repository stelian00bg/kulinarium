//
//  DescriptionTableViewCell.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 28.12.21.
//

import UIKit

class DescriptionTableViewCell: UITableViewCell {
    @IBOutlet weak var descriptionLabel: UILabel!
    
    override func awakeFromNib() {
        backgroundColor = .white
    }
    
    func setDescription(ingredients: String, description: String) {
        descriptionLabel.text = String(format: "Ingredients:\n%@\nHow to prepare:\n%@", parseString(string: ingredients, splitSeparator: " "), description)
    }
    
    func parseString(string: String, splitSeparator: Character) -> String {
        let splitString = string.split(separator: " ")
        var newString = ""
        var counter = 1
        
        splitString.forEach { substring in
            newString.append(String(format: "%d. %@\n", counter, substring.replacingOccurrences(of: "-", with: " ")))
            counter += 1
        }
        
        print("New string is \(newString)")
        return newString
    }
}
