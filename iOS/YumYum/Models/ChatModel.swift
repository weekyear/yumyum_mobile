//
//  ChatModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/05/16.
//

import Foundation


struct Chat: Codable {
    var message: String?
    var geohash: String?
    var lat: Double?
    var lng: Double?
}
