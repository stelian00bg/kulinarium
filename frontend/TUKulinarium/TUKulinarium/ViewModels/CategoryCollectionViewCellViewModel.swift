//
//  CategoryCollectionViewCellViewModel.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import Foundation

class CategoryCollectionViewCellViewModel {
    let imagePath: String
    let categoryName: String
    
    init(imagePath: String, categoryName: String) {
        self.imagePath = imagePath
        self.categoryName = categoryName
    }
}
