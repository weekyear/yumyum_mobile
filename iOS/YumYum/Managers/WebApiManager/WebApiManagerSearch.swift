//
//  WebApiManagerSearch.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/12.
//

import Foundation
import Alamofire
import SwiftyJSON


extension WebApiManager {
    func searchFeed(userId: String, searchKey: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void)  {
        let url = "\(domainUrl)\(feedUrl)list/title/\(searchKey)/\(userId)/"
        
        AF.request(url, method: .get)
            .responseJSON { (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                    let error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
    func searchStore(searchKey: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)place/list/name/\(searchKey)/"
        
        AF.request(url, method: .get)
            .responseJSON { (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                    let error = response.error!
                    failure(error)
                    break
                }
            }
    }
}


