//
//  CategoryCollectionViewCell.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import UIKit
import Kingfisher

class CategoryCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var categoryImageView: UIImageView!
    @IBOutlet weak var blurredBackgroundView: UIVisualEffectView!
    @IBOutlet weak var categoryLabel: UILabel!
    
    var cellViewModel: CategoryCollectionViewCellViewModel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setupUI()
    }
    
    func setImageView() {
        if let url = APIService.shared.cdnAddressURL?.appendingPathComponent(cellViewModel.imagePath) {
            categoryImageView.kf.setImage(with: url)
        }
    }
    
    func setLabel() {
        categoryLabel.text = cellViewModel.categoryName
    }
}
