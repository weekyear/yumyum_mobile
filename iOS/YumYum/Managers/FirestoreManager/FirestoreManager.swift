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
    
    func createChat(userId: Int, chat: [String: Any]) {
        let ref = db.document(String(userId))
        
        ref.setData(chat) { err in
            if let err = err {
                print("Error writing document: \(err)")
            } else {
                print("Document successfully written!")
            }
        }
        
    }
    
    func deleteChat(userId: String) {
        let ref = db.document(userId)
    }
}
