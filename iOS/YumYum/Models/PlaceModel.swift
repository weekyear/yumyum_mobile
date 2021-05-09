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
    var locationX: Double
    var locationY: Double
    var name: String
    var phone: String?
    
    
    init(json: JSON) {
        self.address = json["address_name"].stringValue
        self.locationX = json["x"].doubleValue
        self.locationY = json["y"].doubleValue
        self.phone = json["phone"].stringValue
        self.name = json["place_name"].stringValue

    }
    
    init(address: String, locationX: Double, locationY: Double, name: String, phone: String) {
        
        self.address = address
        self.locationX = locationX
        self.locationY = locationY
        self.name = name
        self.phone = phone
    }
    
    init(feedJson: JSON) {
        self.address = feedJson["place"]["address"].stringValue
        self.locationX = feedJson["place"]["locationX"].doubleValue
        self.locationY = feedJson["place"]["locationY"].doubleValue
        self.name = feedJson["place"]["name"].stringValue
        self.phone = feedJson["place"]["phone"].stringValue

    }
}
