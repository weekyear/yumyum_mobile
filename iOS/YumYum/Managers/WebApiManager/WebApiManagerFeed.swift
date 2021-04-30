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
    func createVideoPath(videoUrl: URL, success: @escaping (JSON) -> Void, failure: @escaping (Error) -> Void) {
        let url = "\(domainUrl)\(feedUrl)video"
        print(url)
        let headers: HTTPHeaders = [
            "Content-Type": "multipart/form-data"
        ]

        
        AF.upload(multipartFormData: { (formData) in
            do {
                let fileName = UUID()
                let videoData = try Data(contentsOf: videoUrl)
                print("hi", fileName, videoUrl, videoData)
                dump(videoData)
                formData.append(videoUrl, withName: "file", fileName: "\(fileName).mp4", mimeType: "video/mp4")
                
            } catch {
                debugPrint("Couldn't get Data from URL: \(videoUrl): \(error)")
            }
        }, to: url, method: .post, headers: headers)
        .response { (response) in
            switch response.result {
            case .success(_):
                let json = JSON(response.data! as Any)
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

