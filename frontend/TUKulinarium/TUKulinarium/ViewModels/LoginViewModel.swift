//
//  LoginViewModel.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 18.12.21.
//

import Foundation

class LoginViewModel {
    var username: String?
    var password: String?
    
    var bindLoginViewModelToController : ((Bool) -> ()) = { isSuccessful in }
    
    init(username: String? = nil, password: String? = nil) {
        self.username = username
        self.password = password
    }
    
    func setUsername(username: String?) {
        self.username = username
    }
    
    func setPassword(password: String?) {
        self.password = password
    }
    
    func login() {
        APIService.loginRequest(requestType: RequstType.post, path: String(format: "%@/%@", "auth", "login"), parameters: ["username" : username, "password": password], headers: ["Content-Type":"application/json"]) { (user, isSuccessful) in
            print("Result is \(user) \(isSuccessful)")
            AppDelegate.user = user
            self.bindLoginViewModelToController(isSuccessful)
        }
    }
}
