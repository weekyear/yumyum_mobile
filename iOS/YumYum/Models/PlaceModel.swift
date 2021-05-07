//
//  PlaceModel.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/30.
//

import Foundation
import SwiftyJSON

struct Place: Codable {
    var address: String
    var locationX: Float
    var locationY: Float
    var name: String
    var phone: String
    
    init(address: String, locationX: Float, locationY:Float, name: String, phone: String) {
        
        self.address = address
        self.locationX = locationX
        self.locationY = locationY
        self.name = name
        self.phone = phone
    }
    
    init(json: JSON) {
        self.address = json["place"]["address"].stringValue
        self.locationX = json["place"]["locationX"].floatValue
        self.locationY = json["place"]["locationY"].floatValue
        self.name = json["place"]["name"].stringValue
        self.phone = json["place"]["phone"].stringValue
    }
}
