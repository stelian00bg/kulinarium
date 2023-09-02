//
//  AppDelegate.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 27.11.21.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    static var user: User?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        UINavigationBar.appearance().barTintColor = .white
        UINavigationBar.appearance().tintColor = .orange
        
        // Attributes for navigation bar buttons
        UIBarButtonItem.appearance().setTitleTextAttributes([NSAttributedString.Key.foregroundColor: UIColor.orange, NSAttributedString.Key.font: UIFont(name: "Noteworthy", size: 20)!], for: UIControl.State.normal)
        
        // Attributes for title
        let attrs = [
            NSAttributedString.Key.foregroundColor: UIColor.orange,
            NSAttributedString.Key.font: UIFont(name: "Noteworthy", size: 22)!
        ]

        UINavigationBar.appearance().titleTextAttributes = attrs
        
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

