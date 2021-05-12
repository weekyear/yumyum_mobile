//
//  PageCell.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import UIKit

class PageCell: UICollectionViewCell {
    
    var tableView: UITableView =  {
        let tableView = UITableView()
        return tableView
    }()
    
//    var collectionView: UICollectionView = {
//        let collectionView = UICollectionView()
//        return collectionView
//    }()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.addSubview(tableView)

    }
}
