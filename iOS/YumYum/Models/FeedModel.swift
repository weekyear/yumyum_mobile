//
//  FeedModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import Foundation


struct Feed: Codable {
    var content: String?
    var place: Place?
    var score: Int?
    var thumbnailPath: URL? = nil
    var title: String?
    var userId: Int?
    var videoPath: URL? = nil
    
    enum CodingKeys: String, CodingKey {
        case content
        case score
        case thumbnailPath
        case title
        case userId
        case videoPath
        case place = "placeRequest"
    }
    
}


