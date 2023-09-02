//
//  TableViewExtension.swift
//  TUKulinarium
//
//  Created by Kristina Simova on 28.12.21.
//

import UIKit

extension UITableView {
    func registerCell(nibName: CellReuseIdentifier) {
        self.register(UINib(nibName: nibName.rawValue, bundle: nil), forCellReuseIdentifier: nibName.rawValue)
    }
    
    func dequeueCell(withIdentifier identifier: CellReuseIdentifier, for indexPath: IndexPath) -> UITableViewCell {
        return self.dequeueReusableCell(withIdentifier: identifier.rawValue, for: indexPath)
    }
}
