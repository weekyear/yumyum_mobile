//
//  MainTabBarVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/20.
//

import UIKit


class MainTabBarVC: UITabBarController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // delegate 설정
        self.delegate = self
    }
    
    
    
}



extension MainTabBarVC: UITabBarControllerDelegate {
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        switch tabBarController.selectedIndex {
        case 0:
            print("0입니다")
        case 1:
            print("1입니다")
        case 2:
            print("2다")
        default:
            print("gg")
        }
    }
}

