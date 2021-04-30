//
//  ReviewVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import UIKit

class ReviewVC: UIViewController {
    
    static func instance() -> ReviewVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "ReviewVC") as! ReviewVC
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
        
        setLayout()
    }
    
    
    func setLayout() {
        // navigationBar
        self.navigationController?.title = "리뷰쓰기"
        self.navigationController?.navigationBar.isHidden = false
        
        self.navigationItem.leftBarButtonItem = UIBarButtonItem(title: "취소", style: .plain, target: self, action: #selector(leftHandAction))
    }
    
    @objc
    func leftHandAction() {
        print("도로마무 불가능")
//        let viewControllers: [UIViewController] = self.navigationController?.viewControllers as [UIViewController]
//        self.navigationController?.popToViewController(viewControllers[viewControllers.count - 3], animated: true)
//        self.navigationController?.dismiss(animated: true, completion: nil)
//        print(viewControllers)
//        print(viewControllers[viewControllers.count - 3])
    }


}
