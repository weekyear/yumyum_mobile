//
//  MypageVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/30.
//

import UIKit

//protocol SendDataDelegate {
//    func sendData(_ viewController: MypageVC, feedList: [Feed])
//}

class MypageVC: UIViewController {
    
    let cellIdentifire : String = "cell"
    var myFeedList: [Feed] = []
    var myLikeFeedList: [Feed] = []
    var tempList: [Feed] = []
    var isCheckFeedList = false
//    var delegate: SendDataDelegate?
    
    static func instance() -> MypageVC {
        let vc = UIStoryboard.init(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MypageVC") as! MypageVC
        return vc
    }
 
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var myProfileImgView: UIImageView!
    
    @IBOutlet weak var myIntroduceLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        self.collectionView.collectionViewLayout = flowLayout
    }
     
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.initTitle()
        self.presentuserData()
        imageMakeRouded(imageview: myProfileImgView)
        self.loadData()
    }
    
    @IBAction func didChangeSegment(_ sender: UISegmentedControl) {
        if sender.selectedSegmentIndex == 0 {
            isCheckFeedList = false
            self.myFeedList = self.tempList
            self.collectionView.reloadData()
        } else if sender.selectedSegmentIndex == 1 {
            isCheckFeedList = true
            self.tempList = self.myFeedList
            self.myFeedList = self.myLikeFeedList
            self.collectionView.reloadData()
        }
    }
    
    func loadData() {
        let userId = UserDefaults.getLoginedUserInfo()!["id"].intValue
        WebApiManager.shared.getMyFeedList(userId: userId, authorId: userId){
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.myFeedList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.tempList = results.arrayValue.compactMap({Feed(feedJson: $0)})
                self.collectionView.reloadData()
            }
        } failure: { (error) in
            print(error.localizedDescription)
        }
        
        WebApiManager.shared.getMyLikeFeed(userId: userId) {
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.myLikeFeedList = results.arrayValue.compactMap({Feed(json: $0)})
            } else {
                print("좋아요 피드 설정 오류 ")
            }
        } faliure: { (error) in
            print(error.localizedDescription)
        }
    }
    
    func initTitle() {
        let userData  = UserDefaults.getLoginedUserInfo()
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 25) // 폰트크기
        nTitle.text = userData!["nickname"].stringValue
        self.navigationItem.titleView = nTitle
    }
    
    func presentuserData(){
        let userData  = UserDefaults.getLoginedUserInfo()
        self.myIntroduceLabel.text = userData!["introduction"].stringValue
        
        if let url = URL(string: userData!["profilePath"].stringValue) {
            var image: UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data: data!)
                    self.myProfileImgView.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(named: "ic_profile")
            self.myProfileImgView.image = image
        }
    }
}

//콜랙션 뷰 셀의 내용과 갯수를 정의한다.
extension MypageVC: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.myFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let reverseMyFeedList = Array(self.myFeedList.reversed())
        let myfeed = reverseMyFeedList[indexPath.item]
        let imageurl:URL = myfeed.thumbnailPath!
        let cell: MyCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! MyCollectionViewCell
        
        var image: UIImage?

        DispatchQueue.global().async {
            let data = try? Data(contentsOf: imageurl)
            DispatchQueue.main.async {
                let beforeimage = UIImage(data: data!)
                image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                if myfeed.isCompleted! == false {
                    let opacityimage = image?.image(alpha: 0.3)
                    cell.foodImageView.image = opacityimage
                } else {
                    cell.foodImageView.image = image
                }
            }
        }
        return cell
    }
}

// 셀을 클릭했을때 처리할 로직 구현
extension MypageVC: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        print("You tepped me \(indexPath.item)")
//        delegate?.sendData(self, feedList: tempList)
//        let myFeedVC = MyFeedVC.instance()
//        self.delegate = myFeedVC
        let vc = UIStoryboard(name: "MyPage", bundle: nil).instantiateViewController(withIdentifier: "MyFeedVC") as! MyFeedVC
        
        if isCheckFeedList == false {
            vc.myFeedList = myFeedList
        } else {
            vc.myFeedList = myLikeFeedList
        }
        vc.itemId = indexPath.item
        vc.modalPresentationStyle = .fullScreen
        self.present(vc, animated: true)
        
        }
    
    }
// 셀의 레이아웃 정하기
extension MypageVC: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        
        let CvRect = collectionView.frame
        
        return CGSize(width: (CvRect.width/3)-3,
                      height: (CvRect.width/2)-3)
    }
    // 패딩을 1로 준다.
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
}
