//
//  RegisterViewController.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import UIKit

class RegisterViewController: UIViewController {
    @IBOutlet weak var nameTextField: DesignableUITextField!
    @IBOutlet weak var usernameTextField: DesignableUITextField!
    @IBOutlet weak var emailTextField: DesignableUITextField!
    @IBOutlet weak var passwordTextField: DesignableUITextField!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var errorRegisterLabel: UILabel!
    
    var registerViewModel: RegisterViewModel = RegisterViewModel()
    
    private var timer: Timer?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .white
        setButton()
        setTextFields(textField: nameTextField, placeholder: "Name")
        setTextFields(textField: usernameTextField, placeholder: "Username")
        setTextFields(textField: emailTextField, placeholder: "Email")
        setTextFields(textField: passwordTextField, placeholder: "Password")
        
        errorRegisterLabel.isHidden = true
        
        registerViewModel.bindRegisterViewModelToController = { [weak self] badRequest in
            print("Bind function called in register")
            self?.setErrorLabel(request: badRequest)
        }
        
    }
    
    private func setButton() {
        registerButton.setDefaultButtonFrame()
        registerButton.setTitle("REGISTER", for: .normal)
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
    
    private func setErrorLabel(request: BadRequest?) {
        DispatchQueue.main.async { [weak self] in
            print("Setting error in register")
            guard let _self = self else { return }
            
            _self.errorRegisterLabel.isHidden = false
            if(request == nil) {
                // go to login screen
                print("Registration successful")
                _self.errorRegisterLabel.text = "Registration succcessful!"
                _self.errorRegisterLabel.textColor = .green
                _self.timer?.invalidate()
                _self.timer = Timer.scheduledTimer(withTimeInterval: 4.0, repeats: false) { _ in
                    _self.performSegue(withIdentifier: "showLoginIdentifier", sender: _self)
                }
            } else {
                print("Registration unsuccessful")
                self?.errorRegisterLabel.text = "Fields must not be empty and password must contain letters and numbers!"
                self?.errorRegisterLabel.textColor = .red
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showLoginIdentifier" {
            if let nextViewController = segue.destination as? LoginViewController {
                nextViewController.navigationController?.navigationBar.isHidden = true
            }
        }
    }
    
    @IBAction func didTapRegisterButton(_ sender: UIButton) {
        print("Name: \(nameTextField.text)")
        print("Username: \(usernameTextField.text)")
        print("Email: \(emailTextField.text)")
        print("Password: \(passwordTextField.text)")
        
        self.registerViewModel.setName(name: nameTextField.text)
        self.registerViewModel.setUsername(username: emailTextField.text)
        self.registerViewModel.setEmail(email: emailTextField.text)
        self.registerViewModel.setPassword(password: passwordTextField.text)
        self.registerViewModel.register()
    }
    
}

// TODO fix animation for hiding/showing keyboard
extension RegisterViewController: UITextFieldDelegate {
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
