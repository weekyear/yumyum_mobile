//
//  WebApiManagerAI.swift
//  YumYumy
//
//  Created by 염성훈 on 2021/05/19.
//

import Foundation
import Alamofire
import SwiftyJSON

extension WebApiManager {
    func postAiVdieo(mediaUrl: URL, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(AiDomainUrl)video"
        let headers: HTTPHeaders = [
            "Content-Type": "multipart/form-data"
        ]
        AF.upload(multipartFormData: {(formData) in
            do {
                let fileName = UUID()
                let mediaStr = "\(mediaUrl)"
                if mediaStr.contains("mp4") {
                    let videoData = try Data(contentsOf: mediaUrl)
                    dump(videoData)
                    formData.append(mediaUrl, withName: "file", fileName: "\(fileName).mp4", mimeType: "video/mp4")
                }
            } catch {
                debugPrint("Couldn't get Data from URL: \(mediaUrl): \(error)")
            }
        }, to: url, method: .post, headers: headers)
        .response { (response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.result as Any)
                success(json)
                break
            case .failure(_):
                let error: Error = response.error!
                failure(error)
                break
            }
        }
    }
    
}
