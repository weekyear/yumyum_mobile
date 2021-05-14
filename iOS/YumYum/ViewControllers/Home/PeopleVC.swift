//
//  PeopleVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/13.
//

import UIKit
import SwiftyJSON

class PeopleVC: UIViewController {
    let cellIdentifire : String  = "peopleCell"
    var peopleFeedList: [Feed] = []
    var peopleLikeList: [Feed] = []
    var userId: Int?
    var username : String?
    var userData: User?
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var ProfileImgView: UIImageView!
    
    @IBOutlet weak var IntroduceLabel : UILabel!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.rightBarButtonItem = nil
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        self.collectionView.collectionViewLayout = flowLayout
        imageMakeRouded(imageview: ProfileImgView)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.initTitle()
        loadData()
        presentUserData()
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
                self.collectionView.reloadData()
            }
            
        } failure: { (error) in
            print("유저 피드리스트 에러 : \(error)")
        }
        
        WebApiManager.shared.getMyLikeFeed(userId: userId!) {
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.peopleLikeList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.collectionView.reloadData()
            }
            
        } faliure: { (error) in
            print(error.localizedDescription)
        }
    }
    
    func presentUserData() {
        self.IntroduceLabel.text = userData?.introduction!
        
        if let url = URL(string: (userData?.profilePath)!) {
            var image : UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data:data!)
                    self.ProfileImgView.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.ProfileImgView.image = image
        }
        
    }
}

extension PeopleVC: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.peopleFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let reversePeopleFeedList = Array(self.peopleFeedList.reversed())
        let peolplefeed = reversePeopleFeedList[indexPath.item]
        let imageurl:URL = peolplefeed.thumbnailPath!
        
        let cell: PeopleCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! PeopleCell
        
        var image: UIImage?
        
        DispatchQueue.global().async {
            let data = try? Data(contentsOf: imageurl)
            DispatchQueue.main.async {
                let beforeimage = UIImage(data: data!)
                image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                
                cell.foodImgView.image = image
            }
        }
        
        return cell
    }
}

extension PeopleVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let CvRect = collectionView.frame
        
        return CGSize(width: (CvRect.width/3)-3,
                      height: (CvRect.width/2)-3)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
}
