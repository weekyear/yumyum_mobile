//
//  MapVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/04.
//

import UIKit

class MapVC: UIViewController, MTMapViewDelegate {

    static func instance() -> MapVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "MapVC") as! MapVC
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        
    }
    
}
