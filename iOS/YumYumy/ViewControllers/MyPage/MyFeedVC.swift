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
    
    var myFeedList: [Feed] = []
//    var myLikeFeedList: [Feed] = []
    
    @IBOutlet var collectionView: UICollectionView!
    
    let userData = UserDefaults.getLoginedUserInfo()!
    var itemId: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setLayout()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
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
            self.dismiss(animated: true)
        }failure: {
            return
        }
    }
}
