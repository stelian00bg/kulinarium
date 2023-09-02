//
//  RecipeCollectionViewCell.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import UIKit

class RecipeCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var recipeImageView: UIImageView!
    @IBOutlet weak var clockImageView: UIImageView!
    @IBOutlet weak var recipeNameLabel: UILabel!
    @IBOutlet weak var preparationTimeLabel: UILabel!
    @IBOutlet weak var favouriteImageView: UIImageView!
    
    var cellViewModel: RecipeCollectionViewCellViewModel! {
        didSet {
            setImageView()
            setLabels()
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setupUI()
    
        recipeImageView.layer.borderWidth = 1
        recipeImageView.layer.masksToBounds = false
        recipeImageView.layer.borderColor = UIColor.black.cgColor
        recipeImageView.layer.cornerRadius = recipeImageView.frame.height/2
        recipeImageView.clipsToBounds = true
        
        clockImageView.tintColor = .orange
        favouriteImageView.tintColor = .orange
    }
    
    func setImageView() {
        if let imagePath = cellViewModel.recipe.images.first?.imageLink, let url = APIService.shared.cdnAddressURL?.appendingPathComponent(imagePath) {
            recipeImageView.kf.setImage(with: url)
        }
    }
    
    func setLabels() {
        recipeNameLabel.text = cellViewModel.recipe.name
        preparationTimeLabel.text = String(format: "%d minutes", cellViewModel.recipe.cookingTime)
    }
}
