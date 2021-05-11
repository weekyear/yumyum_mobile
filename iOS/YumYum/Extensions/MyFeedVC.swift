//
//  MyFeedVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/11.
//

import UIKit

class MyFeedVC: UIViewController {
    
    var myFeedList: [Feed] = []
    var myLikeFeedList: [Feed] = []
    
    let userData = UserDefaults.getLoginedUserInfo()!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        loadData()
    }
    
    func loadData() {
        let userId = userData["id"].intValue
        WebApiManager.shared.getMyFeedList(userId: userId, authorId: userId) { (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.myFeedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
            } else {
                print("내가 작성한 피드를 불러오는데 오류 발생")
            }
        } failure: { (error) in
            print("에러에러")
            print("feed list error: \(error)")
        }
        WebApiManager.shared.getMyLikeFeed(userId: userId){ (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.myLikeFeedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
            }
        } faliure: { (error) in
            print("내가 좋아요한 피드 리스트 에러 \(error)")
        }
    }
}

extension MyFeedVC: UICollectionViewDataSource  {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        return 10
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "mycell", for: indexPath)
        
        return cell
    }
}
