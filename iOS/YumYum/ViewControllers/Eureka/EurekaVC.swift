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
    var locationManager = CLLocationManager()
    
    
    //위도와 경도
    var latitude: Double?
    var longitude: Double?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // 델리게이트 설정
        locationManager.delegate = self
        // 거리 정확도 설정
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        // 사용자에게 허용 받기 alert 띄우기
        locationManager.requestWhenInUseAuthorization()
        
        // 아이폰 설정에서의 위치 서비스가 켜진 상태라면
        if CLLocationManager.locationServicesEnabled() {
            print("위치 서비스 On 상태")
            locationManager.startUpdatingLocation() //위치 정보 업데이트
            // 위도 경도 가져오기
            let coor = locationManager.location?.coordinate
            latitude = coor?.latitude
            longitude = coor?.longitude
        } else {
            print("위치 서비스 Off 상태")
        }
        

        let location = CLLocationCoordinate2D(latitude: latitude!, longitude: longitude!)
        
        let hash = GFUtils.geoHash(forLocation: location)
        
        let chat: [String: Any] = [
            "lat": latitude,
            "lng": longitude,
            "geohash": hash,
            "message": "아아 마이크테스트"
        ]
        
        FirestoreManager.shared.createChat(userId: 24, chat: chat)
        
        
    }
    
    
}


extension EurekaVC: CLLocationManagerDelegate {
    // 위치 정보 계속 업데이트 -> 위도 경도 받아옴
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        print("didUpdateLocations")
        if let location = locations.first {
            print("위도: \(location.coordinate.latitude)")
            print("경도: \(location.coordinate.longitude)")
        }
    }
    
    // 위도 경도 받아오기 에러
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)
    }
}
