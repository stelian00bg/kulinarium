//
//  CollectionViewExtension.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 19.12.21.
//

import UIKit

extension UICollectionView {
    func registerCell(nibName: CellReuseIdentifier) {
        self.register(UINib(nibName: nibName.rawValue, bundle: nil), forCellWithReuseIdentifier: nibName.rawValue)
    }
    
    func registerSupplementaryView(nibName: CellReuseIdentifier, forSupplementaryViewOfKind: String) {
        self.register(UINib(nibName: nibName.rawValue, bundle: nil), forSupplementaryViewOfKind: forSupplementaryViewOfKind, withReuseIdentifier: nibName.rawValue)
    }
    
    func dequeueCell(withIdentifier identifier: CellReuseIdentifier, for indexPath: IndexPath) -> UICollectionViewCell {
        return self.dequeueReusableCell(withReuseIdentifier: identifier.rawValue, for: indexPath)
    }
    
    func dequeueSupplementaryView(ofKind kind: String, withReuseIdentifier reuseIdetifier: CellReuseIdentifier, for indexPath: IndexPath) -> UICollectionReusableView {
        return self.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: reuseIdetifier.rawValue, for: indexPath)
    }
}
