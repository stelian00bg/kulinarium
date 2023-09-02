//
//  Recipe.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import Foundation

struct Recipe: Codable {
    let id: Int
    let cookingTime: Int
    let name: String
    let createdOn: String
    let instructions: String
    let ingredients: String
    let user: UserBase
    let images: [Image]
    let comments: [String]
    let category: Category
}
