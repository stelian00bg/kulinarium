//
//  CollectionViewCellExtension.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 22.12.21.
//

import UIKit

extension UICollectionViewCell {
    func setupUI() {
        contentView.backgroundColor = .clear
        contentView.layer.cornerRadius = 16.0
        contentView.layer.masksToBounds = true
        contentView.layer.borderWidth = 1
        contentView.layer.borderColor = UIColor.black.cgColor
        
        // Shadow
        layer.shadowColor = UIColor.gray.cgColor
        layer.shadowOpacity = 0.4
        layer.shadowOffset = CGSize.zero
        
        layoutSubviews()
    }
}
