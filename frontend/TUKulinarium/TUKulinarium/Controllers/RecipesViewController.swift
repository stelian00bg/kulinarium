//
//  RecipesViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import UIKit

class RecipesViewController: UIViewController {
    @IBOutlet weak var recipesCollectionView: UICollectionView!
    var recipeViewModel: RecipeViewModel!
    
    var selectedIndexPath: IndexPath?
    
    private lazy var categoriesCollectionViewFlowLayout: UICollectionViewFlowLayout = {
        let layout = createFlowLayout()
        layout.scrollDirection = .vertical
        return layout
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .white
        setupCollectionView()
        recipeViewModel.bindRecipeViewModelToController = {
            DispatchQueue.main.async { [weak self] in
                self?.recipesCollectionView.reloadData()
            }
        }
        
    }
    
    func setupCollectionView() {
        recipesCollectionView.collectionViewLayout = categoriesCollectionViewFlowLayout
        recipesCollectionView.backgroundColor = .white
        recipesCollectionView.delegate = self
        recipesCollectionView.dataSource = self
        recipesCollectionView.registerCell(nibName: .RecipeCollectionViewCell)
    }
    
    private func createFlowLayout() -> UICollectionViewFlowLayout {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = 10
        layout.minimumInteritemSpacing = 10
        layout.sectionInset = UIEdgeInsets(top: 0.0, left: 16.0, bottom: 0.0, right: 16.0)
        return layout
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showRecipeDetailsIdentifier" {
            print("Preparing for segue with id showRecipeDetailsIdentifier")
            if let nextViewController = segue.destination as? RecipeDetailsViewController, let indexPath = self.selectedIndexPath, let recipe = self.recipeViewModel.recipes[safe: indexPath.row] {
                nextViewController.recipeDeitalsViewModel = RecipeCollectionViewCellViewModel(recipe: recipe)
                nextViewController.updateScreen()
            }
        }
    }
    
}

extension RecipesViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return recipeViewModel.recipes.count
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellReuseIdentifier.RecipeCollectionViewCell.rawValue, for: indexPath) as? RecipeCollectionViewCell else {
            return UICollectionViewCell()
        }
        
        if let recipe = recipeViewModel.recipes[safe: indexPath.row] {
            cell.cellViewModel = RecipeCollectionViewCellViewModel(recipe: recipe)
        }
        
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let columns = 2
        let itemWidth = (collectionView.bounds.width - (32) - (CGFloat(columns - 1) * 16.0)) / CGFloat(columns)
        
        return CGSize(width: itemWidth, height: 280)
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        selectedIndexPath = indexPath
        performSegue(withIdentifier: "showRecipeDetailsIdentifier", sender: self)
    }
}
