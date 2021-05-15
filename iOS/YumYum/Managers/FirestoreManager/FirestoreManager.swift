//
//  FirestoreManager.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/16.
//

import Foundation
import FirebaseFirestore

struct FirestoreManager {
    static let shared = FirestoreManager()
    private var db = Firestore.firestore().collection("Chats")
    
    func createChat(userId: String) {
        let ref = db.document(userId)
    }
    
    func deleteChat(userId: String) {
        let ref = db.document(userId)
    }
}
