//
//  PlaceSearchVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/06.
//

import UIKit

protocol PlaceDelegate {
    func setPlace(_ viewController: PlaceSearchVC, place: Place)
}

class PlaceSearchVC: UIViewController {
    
    static func instance() -> PlaceSearchVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "PlaceSearchVC") as! PlaceSearchVC
        return vc
    }
    
    var results: [Place] = []
    var delegate: PlaceDelegate?

    @IBOutlet var resultTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.resultTableView.delegate = self
        self.resultTableView.dataSource = self
    
//        self.navigationItem.hidesBackButton = true
//        self.navigationItem.leftBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: self, action: #selector(leftBarButtonAction))
//
        setSearchBar()
    }
    
    @objc func leftBarButtonAction() {
        self.navigationController?.popViewController(animated: true)
    }
    
    func setSearchBar() {
        let searchBar = UISearchBar()
        self.navigationItem.titleView = searchBar
        searchBar.placeholder = "장소검색"
        searchBar.delegate = self
        
        self.navigationItem.titleView = searchBar
    }

}


extension PlaceSearchVC: UISearchBarDelegate {
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        let searchKey = searchBar.text!
        WebApiManager.shared.searchPlace(searchKey: searchKey) { (result) in
            dump(result["documents"])
            self.results = result["documents"].arrayValue.compactMap({Place(json: $0)})
            self.resultTableView.reloadData()
        } failure: { (error) in
            print(#function, "error: \(error)")
        }
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.text = ""
    }
    
}

extension PlaceSearchVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PlaceSearchCell") as! PlaceSearchCell
        
        cell.nameLabel.text = self.results[indexPath.row].name
        cell.addressLabel.text = self.results[indexPath.row].address
        cell.reviewLabel.text = self.results[indexPath.row].phone
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = MapVC.instance(place: results[indexPath.row])
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}

extension PlaceSearchVC: PlaceToSearchVCDelegate {
    func setPlaceToSearchVC(_ controller: MapVC, place: Place) {
        delegate?.setPlace(self, place: place)
    }
}
