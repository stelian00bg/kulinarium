//
//  RecipeViewModel.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import Foundation

class RecipeViewModel {
    var recipes: [Recipe] = [Recipe]()
    var categoryId: Int
    
    var bindRecipeViewModelToController : (() -> ()) = { }
    
    init(categoryId: Int) {
        self.categoryId = categoryId
        loadRecipes()
    }
    
    func loadRecipes() {
        APIService.searchRecipesRequest(requestType: .get, path: String(format: "%@/%@", "recipe", "search"), parameters: ["perPage":100], headers: ["Content-Type":"application/json"]) { recipes in
            print("search recipes returned... recipes \(recipes)")
            self.recipes.removeAll()
            
            self.recipes = recipes?.filter({ $0.category.id == self.categoryId }) ?? [Recipe]()
            print("Recipes for category with id \(self.categoryId) are \(recipes)")
            
            self.bindRecipeViewModelToController()
        }
    }
}
