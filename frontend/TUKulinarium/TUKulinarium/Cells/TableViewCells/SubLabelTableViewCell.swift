//
//  SubLabelTableViewCell.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 28.12.21.
//

import UIKit

class SubLabelTableViewCell: UITableViewCell {
    @IBOutlet weak var categoryNameLabel: UILabel!
    @IBOutlet weak var cookingTimeLabel: UILabel!
    @IBOutlet weak var clockImageView: UIImageView!
    @IBOutlet weak var categoryImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        setImages()
    }
    
    private func setImages() {
        let image = UIImage(named: "categoryIcon")?.withRenderingMode(.alwaysTemplate)
        clockImageView.tintColor = .orange
        categoryImageView.tintColor = .orange
        categoryImageView.image = image
    }
    
    func setCategoryName(categoryName: String) {
        categoryNameLabel.text = categoryName
    }
    
    func setCookingTime(cookingTime: Int) {
        cookingTimeLabel.text = "\(cookingTime) minutes"
    }
}
