//
//  RecipeDetailsViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 28.12.21.
//

import UIKit

enum RecipeDetailsSection {
    case image, name, details, description
}

class RecipeDetailsViewController: UIViewController {
    @IBOutlet weak var recipeDetailsTableView: UITableView!
    
    var recipeDeitalsViewModel: RecipeCollectionViewCellViewModel!
    let sections: [RecipeDetailsSection] = [.image, .name, .details, .description]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupTableView()
    }
    
    private func setupTableView() {
        recipeDetailsTableView.registerCell(nibName: .ImageTableViewCell)
        recipeDetailsTableView.registerCell(nibName: .LabelTableViewCell)
        recipeDetailsTableView.registerCell(nibName: .SubLabelTableViewCell)
        recipeDetailsTableView.registerCell(nibName: .DescriptionTableViewCell)
        recipeDetailsTableView.backgroundColor = .white
        recipeDetailsTableView.dataSource = self
        recipeDetailsTableView.delegate = self
    }
    
    func updateScreen() {
        DispatchQueue.main.async { [weak self] in
            self?.recipeDetailsTableView.reloadData()
        }
    }
}

extension RecipeDetailsViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return sections.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch sections[indexPath.row] {
        case .image:
            if let cell = tableView.dequeueCell(withIdentifier: .ImageTableViewCell, for: indexPath) as? ImageTableViewCell {
                cell.setImageView(imagePath: recipeDeitalsViewModel.recipe.images.first?.imageLink)
                return cell
            }
        case .name:
            if let cell = tableView.dequeueCell(withIdentifier: .LabelTableViewCell, for: indexPath) as? LabelTableViewCell {
                cell.recipeNameLabel.text = recipeDeitalsViewModel.recipe.name
                return cell
            }
        case .details:
            if let cell = tableView.dequeueCell(withIdentifier: .SubLabelTableViewCell, for: indexPath) as? SubLabelTableViewCell {
                cell.setCategoryName(categoryName: recipeDeitalsViewModel.recipe.category.name)
                cell.setCookingTime(cookingTime: recipeDeitalsViewModel.recipe.cookingTime)
                return cell
            }
        case .description:
            if let cell = tableView.dequeueCell(withIdentifier: .DescriptionTableViewCell, for: indexPath) as? DescriptionTableViewCell {
                cell.setDescription(ingredients: recipeDeitalsViewModel.recipe.ingredients, description: recipeDeitalsViewModel.recipe.instructions)
                return cell
            }
        }
        
        return UITableViewCell()
    }
}
