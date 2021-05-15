//
//  EurekaVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/15.
//

import UIKit
import CoreLocation
import GeoFire
import FirebaseFirestore

class EurekaVC: UIViewController {
    private var db = Firestore.firestore()

    
    override func viewDidLoad() {
        super.viewDidLoad()
        

        let latitude = 51.5074
        let longitude = 0.12780
        let location = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        
        let hash = GFUtils.geoHash(forLocation: location)
        
        let documentData: [String: Any] = [
            "geoHash": hash,
            "message": "아아 마이크테스트"
        ]

        let ref = db.collection("Chats").document("24")

        ref.setData(documentData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            } else {
                print("Document successfully written!")
            }
        }
    }
    

}
