//
//  UserDefaultsExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation


extension UserDefaults {
    //MARK: - 서버 유저 정보 불러오기
    static func getLoginedUserInfo() -> NSDictionary {
        var userData: NSDictionary = NSDictionary()
        if let jsonStr = UserDefaults.standard.dictionary(forKey: "USERDATA"){
            userData = jsonStr as NSDictionary
        }
        return userData
    }
    //MARK: - 유저 이메일 저장
    static func saveLoginUserEmail(_ email:String) {
        let plist = UserDefaults.standard
        plist.set(email, forKey: "USEREMAIL")
        plist.synchronize()
    }
    //MARK: - 유저 이메일 불러오기
    static func getLoginedUserEamil() -> String {
        var userEmail: String = String()
        if let tempEmail = UserDefaults.standard.string(forKey: "USEREMAIL") {
            userEmail = tempEmail as String
        }
        return userEmail
    }
    //MARK: - 서버에서 받은 유저 정보 저장
    static func saveLoginedUserInfo(_ userData: [String:Any]) {
        let plist = UserDefaults.standard
        dump(userData)
        plist.set(userData, forKey: "USERDATA")
        plist.synchronize()

    }
}
