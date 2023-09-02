//
//  User.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 18.12.21.
//

import Foundation

struct User: Codable {
    let refreshToken: String
    let id: Int
    let username: String
    let email: String
    let roles: [String]
    let accessToken: String
    let tokenType: String
}
