//
//  MyFeedVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/11.
//

import UIKit

class MyFeedVC: UIViewController {
    
    static func instance() -> MyFeedVC{
        let vc = UIStoryboard.init(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MyFeedVC") as! MyFeedVC
        return vc
    }
    
    @IBOutlet var collectionView: UICollectionView!
    
    var myFeedList: [Feed] = []
//    var myLikeFeedList: [Feed] = []
    let userData = UserDefaults.getLoginedUserInfo()!
    var itemId: Int?
    var feedLikeOrNot:Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setLayout()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.isHidden = true
    }
    
    func setLayout() {
        collectionView.isPagingEnabled = true
        let flowLayout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        flowLayout.minimumLineSpacing = 0
        flowLayout.minimumInteritemSpacing = 0
        collectionView.collectionViewLayout = flowLayout
        collectionView.contentInsetAdjustmentBehavior = .never
        collectionView.scrollToItem(at: IndexPath(item: itemId!, section: 0), at: UICollectionView.ScrollPosition.centeredHorizontally , animated: false)
    }
}

extension MyFeedVC: UICollectionViewDataSource  {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.myFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let feedReverse = Array(self.myFeedList.reversed())
        let feed = feedReverse[indexPath.item]

        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "mycell", for: indexPath) as! MyFeedCollectionViewCell

        let tapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(backMyPage))
        
        cell.delegate = self
        
        if feedLikeOrNot {
            cell.threeDotBtn.isHidden = true
        } else {
            cell.threeDotBtn.isHidden = false
        }

        cell.backMyPageBtn.isUserInteractionEnabled = true
        cell.backMyPageBtn.tag = indexPath.item
        cell.backMyPageBtn.addGestureRecognizer(tapGestureRecognizer)

        cell.configureVideo(with: feed)
        
        return cell
    }
    
    @objc func backMyPage() {
        self.dismiss(animated: true)
    }
}

extension MyFeedVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cvRect = self.collectionView.frame
        return CGSize(width: cvRect.width, height: cvRect.height)
    }
}

extension MyFeedVC: myFeedCellDelegate {
    
    func deleteFeedToMove(feedId: Int) {
        defaultalert("정말 삭제하시겠어요?"){ () in
            WebApiManager.shared.deleteMyFeed(feedId: feedId){
                (result) in
                if result["status"] == "200" {
                    print("피드가 삭제 되었습니다.")
                } else {
                    print("피드 삭제 오류")
                }
            } faliure: { (error) in
                print(error)
            }
            self.dismiss(animated: true)
        }failure: {
            return
        }
    }
    
    func updateFeedToMove(feed : Feed) {
        defaultalert("피드를 수정하시겠습니까?"){ () in
            let reviewVC = UIStoryboard(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "ReviewVC") as! ReviewVC
            reviewVC.updatefeed = feed
            self.navigationController?.pushViewController(reviewVC, animated: true)
            return
        } failure: {
            return
        }
    }
    
}
