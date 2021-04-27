//
//  HomeViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/17.
//

import UIKit
import GoogleSignIn

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    let cellIdentifier: String = "cell"
    @IBOutlet var collectionView: UICollectionView!
    
    var dataset = [
        ("라면", "YEOM", "맜있었습니다", "대전 유성구 계룡로 84", "김밥천국", "mp4", "video"),
        ("돈까스", "WeekYear", "맛없었어요", "대전 유성구 계룡로 11", "경양카츠", "mp4", "video2"),
        ("김밥", "JEYU", "별로였어요", "대전 유성구 계룡로 20", "김밥천국", "mp4", "video3"),
        ("샐러드", "Ahyeon", "존맛탱", "대전 유성구 계룡로 80", "샐러디", "mp4", "video"),
        ("소고기", "YEOMYEOM", "그냥그랬어요", "대전 유성구 계룡로 30", "고깃집", "mp4", "video4")
    ]
    
    lazy var list:[VideoVO] = {
        var datalist = [VideoVO]()
        for (foodTitle, userName, review, addressName, placeName, videoFileFormat, videoFileName) in self.dataset {
            let model = VideoVO()
            model.foodTitle =  foodTitle
            model.userName = userName
            model.review = review
            model.addressName = addressName
            model.placeName = placeName
            model.videoFileFormat = videoFileFormat
            model.videoFileName = videoFileName
            datalist.append(model)
        }
        return datalist
     }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.isPagingEnabled = true
        
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = 0
        flowLayout.minimumInteritemSpacing = 0
        
        self.collectionView.collectionViewLayout = flowLayout
    }
    
    // 해당 row에 이벤트가 발생했을때 출력되는 함수
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        print("You tapped me \(indexPath.row)")
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return list.count
    }
    // 컬렉션 뷰의 지정된 위치에 표시할 셀을 요청하는 메서드
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let model = list[indexPath.row]
        
        let cell: VideoCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifier, for: indexPath) as! VideoCollectionViewCell
        
        cell.configure(with: model)
        
        return cell
    }
    
    // 콜렉션 뷰
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cvRect = collectionView.frame
        return CGSize(width: cvRect.width, height: cvRect.height)
    }
    
}

