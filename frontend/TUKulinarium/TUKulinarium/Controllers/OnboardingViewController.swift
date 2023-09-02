//
//  OnboardingViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 3.12.21.
//

import UIKit

class OnboardingViewController: UIViewController {
    
    @IBOutlet weak var introTitleLabel: UILabel!
    @IBOutlet weak var introSubtitleLabel: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var signInButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setButtons()
        navigationController?.navigationBar.tintColor = .orange
    }
    
    private func setButtons() {
        registerButton.setDefaultButtonFrame()
        registerButton.setTitle("Register".uppercased(), for: .normal)
        signInButton.setDefaultButtonFrame()
        signInButton.setTitle("Login".uppercased(), for: .normal)
    }
    
}
