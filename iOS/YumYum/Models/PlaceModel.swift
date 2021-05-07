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
}
