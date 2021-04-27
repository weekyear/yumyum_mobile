//
//  UserDefaultsExtension.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation

// Mark: Todo - 키이름 대문자로 수정해주세요 forKey: "USERDATA" 이런식으로
extension UserDefaults {
    static func getLoginedUserInfo() {
        if let jsonStr = UserDefaults.standard.string(forKey: "userData") {
            print(jsonStr)
        }
        
    }
}
