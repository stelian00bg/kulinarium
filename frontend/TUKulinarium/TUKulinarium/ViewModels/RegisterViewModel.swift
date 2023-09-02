//
//  RegisterViewModel.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import Foundation

class RegisterViewModel {
    var name: String?
    var username: String?
    var email: String?
    var password: String?
    
    var bindRegisterViewModelToController : ((BadRequest?) -> ()) = { badRequest in }
    
    init(name: String? = nil, username: String? = nil, email: String? = nil, password: String? = nil) {
        self.name = name
        self.username = username
        self.email = email
        self.password = password
    }
    
    func setName(name: String?) {
        self.name = name
    }
    
    func setUsername(username: String?) {
        self.username = username
    }
    
    func setEmail(email: String?) {
        self.email = email
    }
    
    func setPassword(password: String?) {
        self.password = password
    }
    
    func register() {
        APIService.registerRequest(requestType: RequstType.post,
                                   path: String(format: "%@/%@", "auth", "signup"),
                                   parameters: ["name" : name, "username" : username, "password": password, "email" : email],
                                   headers: ["Content-Type":"application/json"]) { (error) in
            print("Result of register is \(error)")
            
            self.bindRegisterViewModelToController(error)
        }
    }
}
