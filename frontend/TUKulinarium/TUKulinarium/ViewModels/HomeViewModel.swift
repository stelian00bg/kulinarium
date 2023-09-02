//
//  HomeViewModel.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import Foundation

class HomeViewModel {
    var categories: [Category] = [Category]()
    
    var bindHomeViewModelToController : (() -> ()) = { }
    
    init() {
        loadCategories()
    }
    
    func loadCategories() {
        APIService.getCategoriesRequest(requestType: .get, path: String(format: "%@/%@", "category", "search"), parameters: [:], headers: ["Content-Type":"application/json"]) { categories in
            print("get categories returned... categories \(categories)")
            self.categories.removeAll()
            self.categories.append(contentsOf: categories ?? [])
            
            self.bindHomeViewModelToController()
        }
    }
}
