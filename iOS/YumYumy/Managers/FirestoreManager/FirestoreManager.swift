//
//  FirestoreManager.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/16.
//

import Foundation
import FirebaseFirestore
import FirebaseFirestoreSwift
import CoreLocation
import GeoFire
import Alamofire
import SwiftyJSON


struct FirestoreManager {
    static let shared = FirestoreManager()
    private var db = Firestore.firestore().collection("Chats")
    
    private var fcmServerKey: String {
        get {
            guard let filePath = Bundle.main.path(forResource: "FCM-Info", ofType: "plist") else {
                fatalError("Couldn't find file 'FCM-Info.plist'.")
            }
            
            let plist = NSDictionary(contentsOfFile: filePath)
            guard let value = plist?.object(forKey: "API_KEY") as? String else {
                fatalError("Couldn't find key 'API_KEY' in 'FCM-Info.plist'.")
            }
            
            return value
        }
    }
    
    
    func createChat(userId: Int, chat: Chat) {
        do {
            try db.document(String(userId)).setData(from: chat)
        } catch let error {
            print("Error writing city to Firestore: \(error)")
        }
    }
    
    func deleteChat(userId: String) {
        let ref = db.document(userId)
    }
    
    
    func getNeighbors(latitude: Double, longitude: Double , completionHandler: @escaping ([String])-> Void) {
        // Find cities within 50km of London
        let center = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        let radiusInKilometers: Double = 500
        
        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.
        let queryBounds = GFUtils.queryBounds(forLocation: center,
                                              withRadius: radiusInKilometers)
        
        
        let queries = queryBounds.compactMap { (any) -> Query? in
            guard let bound = any as? GFGeoQueryBounds else { return nil }
            return db
                .order(by: "geohash")
                .start(at: [bound.startValue])
                .end(at: [bound.endValue])
        }
        
        var matchingDocs = [QueryDocumentSnapshot]()
        var testArr = [String]()
        
        // Collect all the query results together into a single list
        for query in queries {

            query.addSnapshotListener { snapshot, error in
                
                guard let documents = snapshot?.documents else {
                    print("Unable to fetch snapshot data. \(String(describing: error))")
                    return
                }
                
                for document in documents {
                    let lat = document.data()["lat"] as? Double ?? 0
                    let lng = document.data()["lng"] as? Double ?? 0
                    let userId = document.data()["userId"] as? Int ?? 0
                    let message = document.data()["message"] as? String ?? " "
                    
                    
                    let coordinates = CLLocation(latitude: lat, longitude: lng)
                    let centerPoint = CLLocation(latitude: center.latitude, longitude: center.longitude)
                    
                    // We have to filter out a few false positives due to GeoHash accuracy, but
                    // most will match
                    let distance = GFUtils.distance(from: centerPoint, to: coordinates)
             
                    if distance <= radiusInKilometers {

                        print(1)
                        print("여기서왔니?", userId, message)
                        matchingDocs.append(document)
                        testArr.append(message)
                    }
                    
                }
                completionHandler(testArr)
            }
            
        }


    }
    
    func sendMessage(to: String, message: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "https://fcm.googleapis.com/fcm/send"
        print("제발...")
        
        let headers: HTTPHeaders = [
            "Authorization": "key=\(fcmServerKey)",
            "Content-Type": "application/json"
        ]
        
        let parameter: [String : Any] = [
            "to" : "/topics/\(to)",
            "notification" : ["title" : message, "body" : "YUMYUM"],
            "data" : ["body" : "YUMYUM", "title": message]
        ]
        
        
        AF.request(url, method: .post, parameters: parameter, encoding: JSONEncoding.default, headers: headers)
            .response { (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value)
                    success(json)
                    break
                case .failure(_):
                    let error: Error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
}
