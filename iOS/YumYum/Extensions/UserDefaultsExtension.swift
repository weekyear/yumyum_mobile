//
//  UserDefaultsExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import SwiftyJSON

extension UserDefaults {
    
    static func setUserInfo(json:JSON) {
        UserDefaults.standard.setValue(json.rawString(), forKey: LOGINED_USERINFO_USERDEFAULT_KEY)
        UserDefaults.standard.synchronize()
    }
    
    static func getLoginedUserInfo() -> JSON? {
        if let jsonStr = UserDefaults.standard.string(forKey: LOGINED_USERINFO_USERDEFAULT_KEY), let data = jsonStr.data(using: .utf8) {
            do {
                let json = try JSON(data:data)
                return json
            }catch {
                return nil
            }
        }else {
            return nil
        }
    }
    
    static func removeUserData() {
        UserDefaults.standard.removeObject(forKey: LOGINED_USERINFO_USERDEFAULT_KEY)
        UserDefaults.standard.synchronize()
    }
    // MARK: -TODO 아래 함수 쓴 부분 위에 함수로 수정하기
    static func getLoginedUserInfo2() -> NSDictionary {
        var userData = NSDictionary()

        if let jsonStr = UserDefaults.standard.dictionary(forKey: LOGINED_USERINFO_USERDEFAULT_KEY){
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
