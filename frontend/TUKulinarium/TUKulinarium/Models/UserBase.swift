//
//  UserBase.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import Foundation

struct UserBase: Codable {
    let id: Int
    let name: String
    let username: String
    let email: String
    let image: Image
}
