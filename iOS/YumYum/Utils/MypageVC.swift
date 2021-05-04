//
//  MypageVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/30.
//

import UIKit

class MypageVC: UIViewController {
//    var numberOfCell : Int = 10
    let cellIdentifire : String = "cell"
    
    let userJsonData = UserDefaults.getLoginedUserInfo()
    
    var dumyData = [
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6Mjtp1rdyeBKKtnTzZtzDMbt6FVeoCnn4ew&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5R_Jk3aSuKVm5wPJtdfN0PKpqAfZl5CyPLg&usqp=CAU",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeMRzZNEFEvC8p7ovNITF0IvCxBbusMu-mSA&usqp=CAU",
        "http://k4b206.p.ssafy.io/resources/1619775160792video2_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781249745video3_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781263567video4_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619775160792video2_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781249745video3_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781263567video4_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619775160792video2_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781249745video3_thumbnail.png",
        "http://k4b206.p.ssafy.io/resources/1619781263567video4_thumbnail.png",
    ]
    
    lazy var foodImageList: [MyPageModel] = {
        var datalist = [MyPageModel]()
        for (imgurl) in self.dumyData {
            let model = MyPageModel()
            model.foodImageUrl = imgurl
            datalist.append(model)
        }
        return datalist
    }()
 
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var myProfileImgView: UIImageView!
    
    @IBOutlet weak var myIntroduceLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        // 이거 안해주면 난리남 까먹지말자.. 콜렉션뷰에 레이아웃을 적용시키는 부분
        let flowLayout : UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        self.collectionView.collectionViewLayout = flowLayout
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.initTitle()
        self.presentuserData()
        imageMakeRouded(imageview: myProfileImgView)
    }
    
    func initTitle() {
        let nTitle = UILabel(frame:CGRect(x:0, y:0, width: 200, height: 40))
        nTitle.numberOfLines = 1
        nTitle.textAlignment = .center
        nTitle.font = UIFont.systemFont(ofSize: 25) // 폰트크기
        nTitle.text = userJsonData!["nickname"].stringValue
        self.navigationItem.titleView = nTitle
        // 네비게이션 바 배경색상 선택
    }
    
    func presentuserData(){
        self.myIntroduceLabel.text = userJsonData!["introduction"].stringValue
        if let url = URL(string: userJsonData!["profilePath"].stringValue) {
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
            image = UIImage(systemName: "person.crop.circle.badge.plus")
            self.myProfileImgView.image = image
        }
    }
}

//콜랙션 뷰 셀의 내용과 갯수를 정의한다.
extension MypageVC: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.foodImageList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let item = self.foodImageList[indexPath.item]
        
        let cell: MyCollectionViewCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! MyCollectionViewCell
        
        let url: URL! = URL(string: item.foodImageUrl!)
        var image: UIImage?
        
        DispatchQueue.global().async {
            let data = try? Data(contentsOf: url!)
            DispatchQueue.main.async {
                image = UIImage(data: data!)
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
