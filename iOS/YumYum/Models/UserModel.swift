//
//  UserModel.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/28.
//
  
import Foundation
import SwiftyJSON

struct User: Codable {
    var id: Int?
    var profilePath: String?
    var nickname: String?
    var introduction: String?
    var email: String?
    
    init(profilePath: String, nickname: String, introduction: String, email: String) {
        self.profilePath = profilePath
        self.nickname = nickname
        self.introduction = introduction
        self.email = email
    }
    
    init(json: JSON) {
        id = json["id"].intValue
//        profilePath = URL(string: json["profilePath"].stringValue)
        profilePath = json["profilePath"].stringValue
        nickname = json["nickname"].stringValue
        introduction = json["introduction"].stringValue
        email = json["email"].stringValue
    }
}
