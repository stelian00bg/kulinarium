//
//  LoginViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 6.12.21.
//

import UIKit

class LoginViewController: UIViewController {
    
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var usernameTextField: DesignableUITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var errorLoginLabel: UILabel!
    
    var loginViewModel: LoginViewModel = LoginViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setButton()
        setTextFields(textField: usernameTextField, placeholder: "Username")
        setTextFields(textField: passwordTextField, placeholder: "Password")
        errorLoginLabel.isHidden = true
        loginViewModel.bindLoginViewModelToController = { [weak self] isSuccessful in
            print("Bind function called")
            self?.setErrorLabel(isLoginSuccessful: isSuccessful)
        }
        
        navigationItem.title = "Login"
    }
    
    private func setButton() {
        loginButton.setDefaultButtonFrame()
        loginButton.setTitle("LOGIN", for: .normal)
        loginButton.titleLabel?.font = UIFont(name: "Noteworthy Bold", size: 16.0)
    }
    
    private func setTextFields(textField: UITextField, placeholder: String) {
        textField.backgroundColor = .white
        textField.attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [NSAttributedString.Key.foregroundColor: UIColor.black]
        )
        textField.setUnderLine()
        textField.delegate = self
    }
    
    private func setErrorLabel(isLoginSuccessful: Bool) {
        DispatchQueue.main.async { [weak self] in
            if(isLoginSuccessful) {
                // go to home screen
                print("Login successful")
                self?.performSegue(withIdentifier: "showTabBarControllerIdentifier", sender: self)
            } else {
                print("Login unsuccessful")
                self?.errorLoginLabel.isHidden = false
                self?.errorLoginLabel.text = "No such user found!"
                self?.errorLoginLabel.textColor = .red
            }
        }
    }
    
    @IBAction func didTapLoginButton(_ sender: UIButton) {
        print("Email: \(usernameTextField.text)")
        print("Password: \(passwordTextField.text)")
        
        self.loginViewModel.setUsername(username: usernameTextField.text)
        self.loginViewModel.setPassword(password: passwordTextField.text)
        self.loginViewModel.login()
    }
}

// TODO fix animation for hiding/showing keyboard
extension LoginViewController: UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        print("Begin editing")
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return true;
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder();
        return true;
    }
}
