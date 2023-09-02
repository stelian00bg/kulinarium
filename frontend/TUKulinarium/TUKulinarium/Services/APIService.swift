//
//  APIService.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 18.12.21.
//

import Foundation

enum Environment: String, Codable {
    case prod
    case stage
    case test
    case dev
    
    static var all: [Environment] { return [.prod, .stage, .test, .dev] }
}

enum RequstType: String {
    case post = "POST"
    case get = "GET"
    case delete = "DELETE"
}

//enum HTTPHeaderField: String {
//    
//}

enum BackendType: String, Codable {
    case cdn
    case base
}

struct DefaultBackendValues {
    static func baseBackendURL(backend: BackendType) -> String {
        switch backend {
        case .cdn: return "http://192.168.1.6:8081"
        case .base: return "http://192.168.1.6:8080"
        }
    }
    
    static func baseURLString(_ devEnvironment: Environment) -> String {
        switch devEnvironment {
        case .prod:     return ""
        case .stage: return ""
        case .test: return ""
        case .dev:  return DefaultBackendValues.baseBackendURL(backend: .base)
        }
    }
}

class APIService {
    static let shared = APIService()
    
    private init() {}
    
    var hostAddress: String {
        // Leaving it dev, because we do not have test, stage and prod for now
        return DefaultBackendValues.baseURLString(.dev)
    }
    
    var cdnAddress: String {
        return DefaultBackendValues.baseBackendURL(backend: .cdn)
    }
    
    var cdnAddressURL: URL? {
        return URL(string: "\(self.cdnAddress)/image") ?? nil
    }
    
    var apiAddress: String {
        return "\(self.hostAddress)/api"
    }
    
    var apiAddressURL: URL {
        return URL(string: "\(self.apiAddress)")!
    }
    
    static func baseRequest(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil) -> URLRequest {
        let url = APIService.shared.apiAddressURL.appendingPathComponent(path)
        
        var request = URLRequest(url: url)
        request.httpMethod = requestType.rawValue
        
        headers?.forEach({ key, value in
            request.setValue(value, forHTTPHeaderField: key)
        })
        
        print("Base request url is \(url)")
        guard let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: []) else {
            return request
        }
        
        // GET request must not have a httpBody
        if requestType != .get {
            request.httpBody = httpBody
        }
        
        return request
    }
    
    static func baseRequestWithParameters(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil) -> URLRequest {
        guard var components = URLComponents(string: path) else { return URLRequest(url: APIService.shared.apiAddressURL) }
        components.queryItems = [URLQueryItem]()
        
        parameters?.forEach({ (key, value) in
            components.queryItems?.append(URLQueryItem(name: key, value: "\(value)"))
        })
        
        print("Base request with parameters url \(components.url)")
        var request = URLRequest(url: components.url ?? APIService.shared.apiAddressURL)
        request.httpMethod = requestType.rawValue
        
        return request
    }
    
    static func loginRequest(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil, completionHandler: @escaping (User?, Bool) -> Void) {
        let request = APIService.baseRequest(requestType: requestType, path: path, parameters: parameters, headers: headers)
        
        let task = URLSession.shared.dataTask(with: request) {(data, response, error) in
            guard let data = data else { return }
            let decoder = JSONDecoder()
            
            if let json = try? decoder.decode(User.self, from: data) {
                completionHandler(json, true)
            } else {
                completionHandler(nil, false)
            }
        }
    
        task.resume()
    }
    
    static func registerRequest(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil, completionHandler: @escaping (BadRequest?) -> Void) {
        let request = APIService.baseRequest(requestType: requestType, path: path, parameters: parameters, headers: headers)
        
        let task = URLSession.shared.dataTask(with: request) {(data, response, error) in
            guard let data = data else { return }
            let decoder = JSONDecoder()
            
            if let json = try? decoder.decode(BadRequest.self, from: data) {
                completionHandler(json)
            } else {
                completionHandler(nil)
            }
        }
    
        task.resume()
    }
    
    static func getCategoriesRequest(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil, completionHandler: @escaping ([Category]?) -> Void) {
        let request = APIService.baseRequest(requestType: requestType, path: path, parameters: parameters, headers: headers)
        
        let task = URLSession.shared.dataTask(with: request) {(data, response, error) in
            guard let data = data else { return }
            let decoder = JSONDecoder()
            
            print("Executing get categories request")
            if let json = try? decoder.decode([Category].self, from: data) {
                print("JSON \(json)")
                completionHandler(json)
            } else {
                print("JSON not deserialized")
                completionHandler(nil)
            }
        }
    
        task.resume()
    }
    
    static func searchRecipesRequest(requestType: RequstType, path: String, parameters: [String:Any]? = nil, headers: [String:String]? = nil, completionHandler: @escaping ([Recipe]?) -> Void) {
        let request = APIService.baseRequestWithParameters(requestType: requestType, path: String(format: "%@/%@", APIService.shared.apiAddress, path), parameters: parameters, headers: headers)
        
        let task = URLSession.shared.dataTask(with: request) {(data, response, error) in
            guard let data = data else { return }
            let decoder = JSONDecoder()
            
            print("Executing search recipes request")
            if let json = try? decoder.decode([Recipe].self, from: data) {
                print("JSON \(json)")
                completionHandler(json)
            } else {
                print("JSON not deserialized")
                completionHandler(nil)
            }
        }
    
        task.resume()
    }
}

