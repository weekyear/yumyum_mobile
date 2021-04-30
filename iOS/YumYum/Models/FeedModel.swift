//
//  FeedModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import Foundation


struct Feed {
    var userId: Int?
    var title: String?
    var content: String?
    var score: Int?
    var thumbnailPath: URL? = nil
    var videoPath: URL? = nil
    
    //Decodable
    var id: Int?
    var likeCount: Int?
    var isLike: Bool?
    
    // Encodable
    var place: Place?
    
    
    enum CodingKeys: String, CodingKey {
        case userId
        case title
        case content
        case score
        case thumbnailPath
        case videoPath
        case id
        case likeCount
        case isLike
        case place = "placeRequest"
    }
    
}


extension Feed: Decodable {
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
    }
}

extension Feed: Encodable {
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(userId, forKey: .userId)
        try container.encode(title, forKey: .title)
        try container.encode(content, forKey: .content)
        try container.encode(score, forKey: .score)
        try container.encode(thumbnailPath, forKey: .thumbnailPath)
        try container.encode(videoPath, forKey: .videoPath)
        try container.encode(place, forKey: .place)
    }
}
