//
//  MypageVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/30.
//

import UIKit

class MypageVC: UIViewController {
    
    var userModelData = UserModel()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.initTitle()
        // Do any additional setup after loading the view.
        self.loadUserData()
    }
    
    func initTitle() {
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 15) // 폰트크기
        nTitle.text = "사용자명이 표시됩니다."
            
        self.navigationItem.titleView = nTitle
    }
    // UserDafaults에 담겨 있는 데이터를 로드한다.
    func loadUserData() {
        var userData = UserDefaults.getLoginedUserInfo2()
        userModelData.nickName = userData["nickname"] as? String
        userModelData.introduce = userData["introduction"] as? String
        userModelData.profileImg = userData["profilePath"] as? String
        userModelData.userEmail = userData["email"] as? String
    }
    
    
}
