//
//  SearchVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/11.
//

import UIKit

class SearchVC: UIViewController {
    static func instance() -> SearchVC {
        let vc = UIStoryboard.init(name: "Home", bundle: nil).instantiateViewController(withIdentifier: "SearchVC") as! SearchVC
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    


}
