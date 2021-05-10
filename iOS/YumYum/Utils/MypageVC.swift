//
//  MypageVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/30.
//

import UIKit

class MypageVC: UIViewController {
    
    let cellIdentifire : String = "cell"
    var feedList: [Feed] = []
    var myfeedList: [Feed] = []
    var tempList: [Feed] = []
 
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
            self.feedList = self.tempList
            self.collectionView.reloadData()
        } else if sender.selectedSegmentIndex == 1{
            self.tempList = self.feedList
            self.feedList = self.myfeedList
            self.collectionView.reloadData()
        }
    }
    
    func loadData() {
        let userId = UserDefaults.getLoginedUserInfo()!["id"].intValue
        WebApiManager.shared.getMyFeedList(userId: userId, authorId: userId){
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.feedList = results.arrayValue.compactMap({Feed(json: $0)})
                self.collectionView.reloadData()
            }
        } failure: { (error) in
            print(error.localizedDescription)
        }
        
        WebApiManager.shared.getMyLikeFeed(userId: userId) {
            (result) in
            if result["status"] == "200" {
                let results = result["data"]
                self.myfeedList = results.arrayValue.compactMap({Feed(json: $0)})
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
        // 네비게이션 바 배경색상 선택
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
        return self.feedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let imageurl:URL = self.feedList[indexPath.item].thumbnailPath!
        let cell: MyCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! MyCollectionViewCell
        var image: UIImage?

        DispatchQueue.global().async {
            let data = try? Data(contentsOf: imageurl)
            DispatchQueue.main.async {
                let beforeimage = UIImage(data: data!)
                image = beforeimage?.fixedOrientation().imageRotatedByDegrees(degrees: 90.0)
                cell.foodImageView.image = image
            }
        }
        
        
        return cell
    }
}

// 셀을 클릭했을때 처리할 로직 구현
extension MypageVC: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        print("You tepped me \(indexPath.item)")
        moveRootView(name: "MyPage", identifier: "MyFeedVC")
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
