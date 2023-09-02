//
//  DefaultGradientView.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 30.11.21.
//

import UIKit

@IBDesignable
final class DefaultGradientView: GradientView {
    
    override func updateGradient() {
        if let gradient = self.gradient {
            let startColor = self.startColor ?? UIColor.clear
            let endColor = self.endColor ?? UIColor.clear
            gradient.colors = [startColor.cgColor, endColor.cgColor]
            let (start, end) = gradientPointsForAngle(self.angle)
            gradient.startPoint = start
            gradient.endPoint = end
            gradient.frame = self.bounds
        }
    }
    
}
