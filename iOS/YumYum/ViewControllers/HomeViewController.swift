//
//  HomeViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//

import UIKit

class HomeViewController : UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        var images = [
            "image1",
            "image2",
            "image3",
            "image4",
        ]
        
        for i in 0..<images.count {
            let imageView = UIImageView()
            imageView.image = UIImage(named: images[i])
            imageView.contentMode = .scaleAspectFit
            let yPosition = self.view.frame.height * CGFloat(i)

            imageView.frame = CGRect(x: 0, y: yPosition, width: self.view.frame.width, height: self.view.frame.height)

            scrollView.contentSize.height =
                    self.view.frame.height * CGFloat(1+i)
            scrollView.addSubview(imageView)
        }
//        for i in 0..<images.count{
//            let imageView = UIImageView()
//            imageView.image = UIImage(named: images[i])
//            imageView.contentMode = .scaleAspectFit //  사진의 비율을 맞춤.
//            let xPosition = self.view.frame.width * CGFloat(i)
//
//            imageView.frame = CGRect(x: xPosition, y: 0,
//            width: self.view.frame.width,
//            height: self.view.frame.height) // 즉 이미지 뷰가 화면 전체를 덮게 됨.
//
//            scrollView.contentSize.width =
//               self.view.frame.width * CGFloat(1+i)
//
//            scrollView.addSubview(imageView)
//        }

    }
    
}
