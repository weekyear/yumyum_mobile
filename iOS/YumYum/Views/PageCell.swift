//
//  PageCell.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import UIKit


class PageCell: UICollectionViewCell {
    
    @IBOutlet var tableView: UITableView!
    @IBOutlet var collectionView: UICollectionView!
    
    var feedResult: [Feed]?
    var placeResult: [Place]?

    override func awakeFromNib() {
        super.awakeFromNib()
        self.backgroundColor = .gray
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: PlaceTableViewCell.reusableIdentifier, bundle: nil), forCellReuseIdentifier: PlaceTableViewCell.reusableIdentifier)
    }
    

}

extension PageCell: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return placeResult?.count ?? 0
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: PlaceTableViewCell.reusableIdentifier, for: indexPath) as! PlaceTableViewCell
        cell.placeNameLabel.text = placeResult?[indexPath.row].name
        cell.placeAddressLabel.text = placeResult?[indexPath.row].address
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    
}
