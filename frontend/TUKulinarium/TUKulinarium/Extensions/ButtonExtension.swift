//
//  ButtonExtension.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import UIKit

extension UIButton {
    
    func setDefaultButtonFrame() {
        layer.cornerRadius = self.frame.height / 3
        backgroundColor = .orange
        setTitleColor(.white, for: .normal)
        titleLabel?.font = UIFont(name: "Noteworthy Bold", size: 16.0)
    }
    
}
