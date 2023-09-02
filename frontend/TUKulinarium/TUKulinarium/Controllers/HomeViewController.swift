//
//  HomeViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 18.12.21.
//

import UIKit

enum CellReuseIdentifier: String {
    case CategoryCollectionViewCell
    case RecipeCollectionViewCell
    case ImageTableViewCell
    case LabelTableViewCell
    case SubLabelTableViewCell
    case DescriptionTableViewCell
}

class HomeViewController: UIViewController {
    @IBOutlet weak var categoriesCollectionView: UICollectionView!
    
    var homeViewModel: HomeViewModel = HomeViewModel()
    var selectedIndexPath: IndexPath?
    
    private lazy var categoriesCollectionViewFlowLayout: UICollectionViewFlowLayout = {
        let layout = createFlowLayout()
        layout.scrollDirection = .vertical
        return layout
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupCollectionView()
        homeViewModel.bindHomeViewModelToController = {
            DispatchQueue.main.async { [weak self] in
                self?.categoriesCollectionView.reloadData()
            }
        }
        
        navigationItem.title = "Categories"
        navigationController?.navigationBar.isHidden = true
        navigationItem.setHidesBackButton(true, animated: true)
        tabBarController?.navigationItem.hidesBackButton = true
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showRecipeControllerIdentifier" {
            print("Preparing for segue with id showRecipeControllerIdentifier")
            if let nextViewController = segue.destination as? RecipesViewController, let indexPath = self.selectedIndexPath, let category = self.homeViewModel.categories[safe: indexPath.row] {
                nextViewController.recipeViewModel = RecipeViewModel(categoryId: category.id)
            }
        }
    }
    
    func setupCollectionView() {
        categoriesCollectionView.collectionViewLayout = categoriesCollectionViewFlowLayout
        categoriesCollectionView.backgroundColor = .white
        categoriesCollectionView.delegate = self
        categoriesCollectionView.dataSource = self
        categoriesCollectionView.registerCell(nibName: .CategoryCollectionViewCell)
    }
    
    private func createFlowLayout() -> UICollectionViewFlowLayout {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        layout.minimumLineSpacing = 10
        layout.minimumInteritemSpacing = 10
        layout.sectionInset = UIEdgeInsets(top: 0.0, left: 0.0, bottom: 0.0, right: 0.0)
        layout.itemSize = CGSize(width: 300, height: 300)
        return layout
    }
}

extension HomeViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return homeViewModel.categories.count
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CellReuseIdentifier.CategoryCollectionViewCell.rawValue, for: indexPath) as? CategoryCollectionViewCell else {
            return UICollectionViewCell()
        }
        
        if let category = homeViewModel.categories[safe: indexPath.row] {
            cell.cellViewModel = CategoryCollectionViewCellViewModel(imagePath: category.imageLink, categoryName: category.name)
            cell.setImageView()
            cell.setLabel()
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: UIScreen.main.bounds.width - 32, height: 200)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        selectedIndexPath = indexPath
        performSegue(withIdentifier: "showRecipeControllerIdentifier", sender: self)
    }
}

