//
//  HomeViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/17.
//

import UIKit
import GoogleSignIn
import SwiftyJSON

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    static func instance() -> HomeViewController {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "HomeVC") as! HomeViewController
        return vc
    }
    
    let cellIdentifier: String = "cell"
    @IBOutlet var collectionView: UICollectionView!
    
    var feedList: [Feed] = []
        
    override func viewDidLoad() {
        super.viewDidLoad()
        collectionView.isPagingEnabled = true
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = 0
        flowLayout.minimumInteritemSpacing = 0
        self.collectionView.collectionViewLayout = flowLayout
        
         let userId = UserDefaults.getLoginedUserInfo()!["id"].intValue
            WebApiManager.shared.getFeedList(userId: userId) { (result) in
                if result["status"] == "200" {
                    let results = result["data"]
//                    print(results)
                    self.feedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                    self.collectionView.reloadData()
                }
            } failure: { (error) in
                print("feed list error: \(error)")
            }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.collectionView.reloadData()
    }
    
    // 해당 row에 이벤트가 발생했을때 출력되는 함수
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        print("You tapped me \(indexPath.row)")
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.feedList.count
    }
    
    // 컬렉션 뷰의 지정된 위치에 표시할 셀을 요청하는 메서드
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let feedReverse = Array(feedList.reversed())
        let feed = feedReverse[indexPath.row]
        
        let cell: VideoCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifier, for: indexPath) as! VideoCollectionViewCell
        
        cell.configureVideo(with: feed)
        
        return cell
    }
    
    // 콜렉션 뷰 레이아웃 조정 메서드
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cvRect = collectionView.frame
        return CGSize(width: cvRect.width, height: cvRect.height)
    }
}

