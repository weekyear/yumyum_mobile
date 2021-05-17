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

struct FirestoreManager {
    static let shared = FirestoreManager()
    private var db = Firestore.firestore().collection("Chats")
    
    func createChat(userId: Int, chat: Chat) {
        let encoder = JSONEncoder()
//        let data = try encoder.encode(chat)
        
        do {
            try db.document(String(userId)).setData(from: chat)
        } catch let error {
            print("Error writing city to Firestore: \(error)")
        }
    }
    
    func deleteChat(userId: String) {
        let ref = db.document(userId)
    }
    
    
    func getNeighbors(latitude: Double, longitude: Double) {
        // Find cities within 50km of London
        let center = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        let radiusInKilometers: Double = 50

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
        
        print("네이벙네이버되나?")
        print(queries)
    
        var matchingDocs = [QueryDocumentSnapshot]()
        
        // Collect all the query results together into a single list
        func getDocumentsCompletion(snapshot: QuerySnapshot?, error: Error?) -> () {
            guard let documents = snapshot?.documents else {
                print("Unable to fetch snapshot data. \(String(describing: error))")
                return
            }

            for document in documents {
                let lat = document.data()["lat"] as? Double ?? 0
                let lng = document.data()["lng"] as? Double ?? 0
                let userId = document.data()["userId"] as? Int ?? 0
                print("여기서왔니?", lat, lng, userId)
                let coordinates = CLLocation(latitude: lat, longitude: lng)
                let centerPoint = CLLocation(latitude: center.latitude, longitude: center.longitude)

                // We have to filter out a few false positives due to GeoHash accuracy, but
                // most will match
                let distance = GFUtils.distance(from: centerPoint, to: coordinates)
                if distance <= radiusInKilometers {
                    print("안녕", document)
                    matchingDocs.append(document)
                }
            }
        }
        
        // After all callbacks have executed, matchingDocs contains the result. Note that this
        // sample does not demonstrate how to wait on all callbacks to complete.
        for query in queries {
            query.getDocuments(completion: getDocumentsCompletion)
        }
        
    }
    


}
