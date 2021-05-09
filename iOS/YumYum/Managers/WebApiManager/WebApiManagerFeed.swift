//
//  WebApiManagerFeed.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    func createFeed(feed: Feed, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)"
        AF.request(url, method: .post, parameters: feed, encoder: JSONParameterEncoder.default)
          .responseJSON {(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
                
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    
    func getFeedList(userId: Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)/list/\(userId)"
        AF.request(url, method: .get)
          .responseJSON {(response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
                
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    
    func getMyFeedList(userId: Int, authorId: Int, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)/list/\(authorId)/\(userId)"
        
        AF.request(url, method: .get)
            .responseJSON{ (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    success(json)
                    break
                case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
                }
            }
    }
    
    func postLikeFeed(likeInfo : userLike, suceess: @escaping (JSON) -> Void,
                      failure : @escaping(Error) -> Void) {

        let url = "\(domainUrl)\(feedUrl)like"

        AF.request(url, method: .post, parameters: likeInfo, encoder: JSONParameterEncoder.default)
            .responseJSON{ (response) in
                switch response.result {
                case .success(_):
                    let json = JSON(response.value!)
                    suceess(json)
                    break
                case .failure(_):
                    let error: Error = response.error!
                    failure(error)
                    break
                }
            }
    }
    
    func getMyLikeFeed(userId : Int, success: @escaping (JSON) -> Void, faliure: @escaping (Error) -> Void) {
        
        let url = "\(domainUrl)\(feedUrl)list/like/\(userId)"
        
        AF.request(url, method: .get).responseJSON{ (response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.value!)
                success(json)
                break
            case .failure(_):
                let error: Error = response.error!
                faliure(error)
                break
            }
            
        }
    }
    
}

