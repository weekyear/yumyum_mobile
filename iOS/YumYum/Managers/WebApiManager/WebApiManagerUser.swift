//
//  WebApiManagerUser.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import Foundation
import Alamofire


// extension지정 하면 WebApiManager를 그대로 가져와 쓸 수 있다.
extension WebApiManager {
    //MARK: - 로그인 API 메서드 서버에 저장된 Email을 리턴한다.
    func userLogin(_ userEmail: String, successHandler: @escaping ([String:Any]) -> Void, failureHandler: @escaping (String) -> Void) {
        let url = "\(domainUrl)\(userUrl)\(userEmail)"
        
//        // struct JSON to Struct
//        AF.request(url).responseDecodable(of: BaseStruct<TestUserData>.self) { (response) in
//            guard let data = response.value?.data else {
//                failureHandler("No Data")
//                return
//            }
//            successHandler(data)
//        }
        let call = AF.request(url, method: HTTPMethod.get, encoding: JSONEncoding.default)

        call.responseJSON { res in
            let result = try! res.result.get()

            guard let jsonObject = result as? NSDictionary else {
                print("딕셔너리 캐스팅 오류")
                return

            }
            if let status = jsonObject["status"] as? String {
                if status == "200" {
                    let userData = jsonObject["data"] as! [String:Any]
                    successHandler(userData)
                } else {
                    failureHandler("status 오류!")
                }
            } else {
                failureHandler("status 오류!")
            }
        }
    }
    //MARK: - 회원가입 API 메서드
    func userSignUp(userData: UserModel, successHandler: @escaping ([String:Any]) -> Void, failureHandler: @escaping (String) -> Void) {
        let param : Parameters = [
            "email" : userData.userEmail!,
            "nickname" : userData.nickName!,
            "introduction" : userData.introduce!,
            "profilePath" : userData.profileImg!
        ]
        
        let url = "\(domainUrl)\(signUpUrl)"
        let call = AF.request(url, method: HTTPMethod.post,
                              parameters: param, encoding: JSONEncoding.default)
        
        call.responseJSON{ res in
            let result = try! res.result.get()
            
            guard let jsonObject = result as? NSDictionary else {
                print("JSON타입 캐스팅 오류!!")
                return
            }
            
            if let status = jsonObject["status"] as? String {
                if status == "200" {
                    print("회원가입 포스트성공")
                    let serverUserData = jsonObject["data"] as! [String:Any]
                    successHandler(serverUserData)
                } else {
                    print("회원가입 실패")
                }
            }
        }
    }
}
