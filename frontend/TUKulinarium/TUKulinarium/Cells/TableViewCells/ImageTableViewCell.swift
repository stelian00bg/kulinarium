//
//  ImageTableViewCell.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 28.12.21.
//

import UIKit

class ImageTableViewCell: UITableViewCell {
    @IBOutlet weak var recipeImageView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setImageView(imagePath: String?) {
        if let image = imagePath, let url = APIService.shared.cdnAddressURL?.appendingPathComponent(image) {
            recipeImageView.kf.setImage(with: url)
        }
    }
}
