//
//  WebApiManagerUser.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    func checkUser(userEmail: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)email/\(userEmail)"
        AF.request(url, method: .get)
            .responseJSON { (response) in
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

    func login(userEmail: String, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(userUrl)login/\(userEmail)"
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
    
    func signup(user: User, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {

        let url = "\(domainUrl)\(signUpUrl)"
        
        AF.request(url, method: .post, parameters: user, encoder: JSONParameterEncoder.default)
          .responseJSON { (response) in
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
    //MARK: - 프로필사진 URL을 반환하는 메서드
    func createProfilePath() {
        
        }
}
