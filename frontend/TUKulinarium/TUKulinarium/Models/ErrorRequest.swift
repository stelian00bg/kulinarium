//
//  ErrorRequest.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import Foundation

struct BadRequest: Codable {
    let timestamp: String
    let status: Int
    let error: String
    let path: String
}
