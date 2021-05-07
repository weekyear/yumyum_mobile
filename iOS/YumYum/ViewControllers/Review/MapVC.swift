//
//  MapVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/04.
//

import UIKit

class MapVC: UIViewController, MTMapViewDelegate {

    static func instance(place: Place) -> MapVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "MapVC") as! MapVC
        vc.place = place
        vc.navigationItem.title = place.name
        
        let item : MTMapPOIItem = MTMapPOIItem()
        item.itemName = "Marker"
        item.showDisclosureButtonOnCalloutBalloon = true
        item.markerSelectedType = .redPin
        item.mapPoint = MTMapPoint(geoCoord: MTMapPointGeo(latitude: place.locationX, longitude:  place.locationY))
        
        vc.map.add(item)
        vc.map.fitAreaToShowAllPOIItems()
        
        return vc
    }
    
    var place: Place?
    var poiItem: MTMapPOIItem?
    
    @IBOutlet var map: MTMapView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        map.showCurrentLocationMarker = true
        
        
    }
    
}
