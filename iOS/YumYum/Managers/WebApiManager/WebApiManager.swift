//
//  WebApiManager.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire

class WebApiManager: NSObject {
    static let shared = WebApiManager()
    
    let domainUrl = "http://k4b206.p.ssafy.io:8081/yumyum/"
    let userUrl = "user/login/"
    

    override private init() {
        super.init()
    }
}
