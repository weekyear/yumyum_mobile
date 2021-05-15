//
//  FirestoreManager.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/16.
//

import Foundation
import FirebaseFirestore
import FirebaseFirestoreSwift


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
}
