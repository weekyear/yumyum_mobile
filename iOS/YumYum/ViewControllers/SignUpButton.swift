//
//  SignUpButton.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/17.
//

import UIKit

class SignUpButton: UIButton {

// 스토리보드에서 참고
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)!
        self.layer.cornerRadius = 5.0;
        self.backgroundColor = UIColor(red: 255/255, green: 132/255, blue: 102/255, alpha: 1)
        self.tintColor = UIColor.white
    }
    
}
