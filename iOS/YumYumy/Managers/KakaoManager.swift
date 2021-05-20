//
//  KakaoManager.swift
//  YumYumy
//
//  Created by Ahyeonway on 2021/05/20.
//

import Foundation
import SwiftyJSON
import KakaoSDKLink
import KakaoSDKTemplate

struct KakaoManager {
    static let shared = KakaoManager()
    
    func shareFeed(feed: Feed) {
        let templateId = Int64(53929)
        
        let templateArgs: [String : String] = [
            "title":"YUMYUM",
            "description": String("\(feed.title) 구경하실래요?"),
            "feedImage": String("\(feed.thumbnailPath)")
        ]
        
        LinkApi.shared.customLink(templateId: templateId, templateArgs: templateArgs) {(linkResult, error) in
            if let error = error {
                print(error)
            }
            else {
                print("customLink() success.")
                if let linkResult = linkResult {
                    UIApplication.shared.open(linkResult.url, options: [:], completionHandler: nil)
                }
            }
        }
        
    }
}
