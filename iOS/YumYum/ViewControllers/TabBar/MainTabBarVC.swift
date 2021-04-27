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
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        if viewController.isKind(of: CameraVC.self) {
            let vc = storyboard?.instantiateViewController(withIdentifier: "Review")
            vc?.modalPresentationStyle = .overFullScreen
            self.present(vc!, animated: true, completion: nil)
            return false
        }
        return true
    }
    
}

