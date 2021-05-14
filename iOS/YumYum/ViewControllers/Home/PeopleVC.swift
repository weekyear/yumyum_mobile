//
//  PeopleVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/13.
//

import UIKit

class PeopleVC: UIViewController {
    let cellIdentifire : String  = "peopleCell"
    var peopleFeedList: [Feed] = []
    var peopleLikeList: [Feed] = []
    var userId: Int?
    var username : String?
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var ProfileImgView: UIImageView!
    
    @IBOutlet weak var IntroduceLabel : UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.rightBarButtonItem = nil
        loadData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.initTitle()
    }
    
    
    func initTitle() {
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 25) // 폰트크기
        nTitle.text = username
        self.navigationItem.titleView = nTitle
    }
    
    func loadData() {
        WebApiManager.shared.getMyFeedList(userId: userId!, authorId: userId!) {(result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.peopleFeedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
            }
            
        } failure: { (error) in
            print("유저 피드리스트 에러 : \(error)")
        }
        
        WebApiManager.shared.getMyLikeFeed(userId: userId!) {
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.peopleLikeList = results.arrayValue.compactMap({Feed(feedJson: $0)})
            }
            
        } faliure: { (error) in
            print(error.localizedDescription)
        }
    }
}

extension PeopleVC: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.peopleFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell: PeopleCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! PeopleCell
        
        return cell
    }
    
    
}
