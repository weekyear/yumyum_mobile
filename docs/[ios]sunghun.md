

# [iOS] sunghun 개발과정

1. Editor ->. refectort to Storyboard 가면 스토리보드를 나눠줄 수 있음.
2. 로그인, 회원가입, 홈 UI,UX 만들기



## 오토레이아웃

- 오토레이아웃을 코드로 작성해서 적용해야할때 반드시,   addSubview로 먼저 버튼을 올리고 진행해야한다. 아래에 작성 

```swift
func addButton() {
        let button = ASAuthorizationAppleIDButton(authorizationButtonType: .signIn, authorizationButtonStyle: .black)
        
        button.addTarget(self, action: #selector(handleAuthorizationAppleIDButtonPress), for: .touchUpInside)
        
        button.translatesAutoresizingMaskIntoConstraints = false
        // 먼저 loginView에 버튼을 올리고 시작한다.
        LoginView.addSubview(button)
        
        button.leadingAnchor.constraint(equalTo: LoginView.leadingAnchor, constant: 30).isActive = true
        
        button.trailingAnchor.constraint(equalTo: LoginView.trailingAnchor, constant: 30).isActive = true
        button.topAnchor.constraint(equalTo: LoginView.topAnchor, constant: 30).isActive = true
        button.bottomAnchor.constraint(equalTo: LoginView.bottomAnchor, constant: 30).isActive = true
    }
```



### 메인스토리 보드 변경하는법

> 여러 스토리보드중 시작 스토리보드를 변경하고 싶을때 참고, General에서 main interface를 바꿔줘하고 **또, 반드시 Info-Application Scene Manifest 에서 해당 스토리 보드명으로 바꿔줘야함!**

- 참고 : https://stackoverflow.com/questions/58074878/xcode-11-main-interface-fixed-with-main-storyboard



## 이미지 플립

> 

```swift
import UIKit

class HomeViewController : UIViewController {
    
    @IBOutlet weak var scrollView: UIScrollView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        var images = [
            "image1",
            "image2",
            "image3",
            "image4",
        ]
        
        for i in 0..<images.count {
            let imageView = UIImageView()
            imageView.image = UIImage(named: images[i])
            imageView.contentMode = .scaleAspectFit
            // y축 포지션을 계속 변경시켜줌
            let yPosition = self.view.frame.height * CGFloat(i)
						// imageView의 프레임을 x,y좌표를 기준으로 정해주고 너비와 높이를 갖는 사격형이다. 
            imageView.frame = CGRect(x: 0, y: yPosition, width: self.view.frame.width, height: self.view.frame.height)
						// 스크롤 뷰의 컨탠츠 사이즈 높이를 정해준다.
            scrollView.contentSize.height =
                    self.view.frame.height * CGFloat(1+i)
            // 스크롤 뷰 위에 올리면 끝, addSubview는 이미지 뷰의
            scrollView.addSubview(imageView)
```

- view를 그리기 위해서는 반드시 알아야할 지식 - CGRect와 CGSized차이 : https://zeddios.tistory.com/201 

### frame과 boundsd의 차이

> 참고 : https://zeddios.tistory.com/203

1) Frame: SuperView(상위뷰)의 좌표시스템 안에서 View의 위치와 크기를 나타냄.

- Frame은 자신의 한단계 상위뷰, 자산의 부모뷰의 origin으로부터 떨어진 곳에 사각형을 그려주게 된다.

2) Bounds: 자신만의 좌표 시스템을 갖는다. 자신의 x,y 좌표를 갖는다. 

```swift
subView.bounds.origin.x = 60

subView.bounds.origin.y = 50
```

- **Bounds는 상위뷰 안에서의 좌표가 아닌 "자신만의 좌표시스템"을 가진다고 그랬죠? Bounds를 변경하는 것은 해당 위치에서 View를 다시그리라는 의미가 돼요. Bounds는 상위뷰와 아무런 관련이 없으므로, subView는 움직이지 않는 것 처럼 보이고 그 안에있던 imageView가 움직이는 것 처럼 보이는 것입니다.** 



## 콜렉션 뷰(UIcollectionView)

### UITableView와 UIcollectionView의 차이?

> 가장큰 차이는 컬렉션 뷰의 셀은 배경뷰가 있어서 배경을 넣고 그위에 콘텐츠를 작성하기에 편리하다는 것이다.

- 테이블뷰 셀의 구조는 콘텐츠 영역과 액세서리뷰 영역으로 나뉘었지만, 컬렉션뷰 셀은 배경뷰와 실제 콘텐츠를 나타내는 콘텐츠뷰로 나뉘었습니다.
- 테이블뷰 셀은 기본으로 제공되는 특정 스타일을 적용할 수 있지만 컬렉션뷰 셀은 특정한 스타일이 따로 없습니다.
- 테이블뷰 셀은 목록형태로만 레이아웃 되지만, 컬렉션뷰 셀은 다양한 레이아웃을 지원합니다.
- 커스텀하게 Layout을 설정할 수 있다. 

- 이런 이유들로 메인페이지에서 동영상을 보여줄떄 `UIcollectionView`를 쓰는게 맞다.

```swift
class HomeViewController : UIViewController{
    // 접근 제어
    private var collectionView: UICollectionView?
    
    private var data = [VideoModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for _ in 0..<10 {
            let model = VideoModel(caption: "delicious chickhen", userName: "Yeom", placeName: "YumYumChickhen", addressName: "대전시 레자미 3차 802호", videoFileName: "video", videoFileFormat: "mp4", review: "노맛!!!!")
            
            data.append(model)
        }
        
        // layout 개겣를 생성하고
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        // video 한개의 크기와 범위를 정해준다.
        layout.itemSize = CGSize(width: view.frame.size.width, height: view.frame.size.height)
        // 레이아웃에 마진주기
        layout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        // 콜랙션 뷰의 객체를 frame 위치에 생성한다. frame : .zero 가로, 세로 0 좌표는 x:0 ,y :0 과 같다.
        collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        // 셀을 콜렉션 뷰에 등록한다.
        collectionView?.register(VideoCollectionViewCell.self, forCellWithReuseIdentifier: VideoCollectionViewCell.identifier)
        // 페이징 적용
        collectionView?.isPagingEnabled = true
        // 콜렉션 뷰를 사용하기 위해서는 반드시 dataSource를 delegate로 지정해줘함.
        collectionView?.dataSource = self
        view.addSubview(collectionView!)
    }
```

### 접근제어

> 코드끼리 상호작용할 때 파일 간 또는 모듈 간에 접근을 제한할 수 있는 기능이다. 접근 제어를 통해 코드의 상세 구현은 숨기고 허용된 기능만 사용하는 인터페이스를 제공할 수 있다.

- public :  자신이 구현된 소스 파일 물론, 그 소스파일이 속해 있는 모듈, 그 모듈을 가져다 쓴느 모듈 등 모든 곳에서 사용할 수 있다. 주로 외부와 연결될 인터페이스 구현을 하는데 많이 쓰인다. Model 파일을 public으로 지정해서 쓰기도함.
- private :  접근 수준이 가장 한정적인 범위, 소스파일 내부에서만 사용할 수있으며, 심지어 같은 파일안에 구현한 다른 타입이나 기능에서도 사용할 수가 없다. 



### viewDidLayoutSubviews

> 뷰가 서브 뷰의 레이아웃을 변경한 후에 호출되는 메서드이다.



### bundle의  path 함수

> 이름과 파일 확장자를 통해서 정의된 리소스의 full pathname을 반환한다.

- 그냥 스트링으로 넣으면 안되나?? 그러면 하드코딩되는 형식이 되서 안된다. 

```swift
let jsonPath = "/Users/kangsujin/Desktop/boostcourse/Weather/Weather/Weather/Data/countries.json"
String(contentsOfFile: jsonPath)
```

- 파일 이름과 타입을 정해주면 파일 경로를 리턴시켜 준다.

```swift
 guard let path = Bundle.main.path(forResource: model.videoFileName, ofType: model.videoFileName) else {
            return
        }
```



### 코드로 콜랙션 뷰 짜기

> 커스텀을 코드로 미세하게 할 수 있는것 같지만 너무 많은 시간과 노력이 들어서 실제 개발할때는 이런 방식으로 하면 안될 것 같습니다. 

- HomeViewController.swift

```swift
//
//  HomeViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//

import UIKit

// 화면에 담길 Label을 모델로 만든다.
struct VideoModel{
    let caption : String
    let userName : String
    let placeName : String
    let addressName : String
    let videoFileName : String
    let videoFileFormat : String
    let review : String
}

class HomeViewController : UIViewController{
    // 접근 제어
    private var collectionView: UICollectionView?
    
    private var data = [VideoModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for _ in 0..<10 {
            let model = VideoModel(caption: "delicious chickhen", userName: "Yeom", placeName: "YumYumChickhen", addressName: "대전시 레자미 3차 802호", videoFileName: "video", videoFileFormat: "mp4", review: "노맛!!!!")
            
            data.append(model)
        }
        
        // layout 개겣를 생성하고
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        // video 한개의 크기와 범위를 정해준다.
        layout.itemSize = CGSize(width: view.frame.size.width, height: view.frame.size.height)
        // 레이아웃에 마진주기
        layout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        // 콜랙션 뷰의 객체를 frame 위치에 생성한다. frame : .zero 가로, 세로 0 좌표는 x:0 ,y :0 과 같다.
        collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        // 셀을 콜렉션 뷰에 등록한다.
        collectionView?.register(VideoCollectionViewCell.self, forCellWithReuseIdentifier: VideoCollectionViewCell.identifier)
        // 페이징 적용
        collectionView?.isPagingEnabled = true
        // 콜렉션 뷰를 사용하기 위해서는 반드시 dataSource를 delegate로 지정해줘함.
        collectionView?.dataSource = self
        view.addSubview(collectionView!)
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        collectionView?.frame = view.bounds
    }
}

extension HomeViewController: UICollectionViewDataSource{

    // 지정된 섹션에 표시할 항목의 갯수를 묻는 매서드.(필수)
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return data.count
    }
    // 콜렉션 뷰의 특정 인덱스에서 표시할 셀을 요청하는 메서드이다.(필수)
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let model = data[indexPath.row]
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: VideoCollectionViewCell.identifier, for: indexPath) as! VideoCollectionViewCell
        
        cell.configure(with: model)
        
        return cell
    }
        
}

```

- VideoCollectionViewCell.swift

```swift
//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/16.
//

// videocell 한개이고 비디오 한 화면에 담기는 것들을 제어한다.
import UIKit
import AVFoundation

protocol VideoCollectionViewCellDelegate : AnyObject{
    func didTapLikeButton(with model: VideoModel)
    func didTapShareButton(with model: VideoModel)
}

class VideoCollectionViewCell: UICollectionViewCell {
    
    static let identifier = "VideoCollectionViewCell"
    
    // Labels 객체
    private let userNameLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .left
        label.textColor = .white
        return label
    }()
    
    private let captionLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .left
        label.textColor = .white
        return label
    }()
    
    private let placeNameLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .left
        label.textColor = .white
        return label
    }()
    
    private let addressNameLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .left
        label.textColor = .white
        return label
    }()
    
    private let reviewLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .left
        label.textColor = .white
        return label
    }()
    
    //Buttons 객체
    private let likeButton: UIButton = {
        let button = UIButton()
        button.setBackgroundImage(UIImage(systemName: "heart.fill"), for: .normal)
        return button
    }()
    
    private let shareButton: UIButton = {
        let button = UIButton()
        button.setBackgroundImage(UIImage(systemName: "arrowshape.turn.up.right.fill"), for: .normal)
        return button
    }()
    
    private let videoContainer = UIView()
    
    
    //Delegate : 버튼의 이벤트를 처리해줄 델리게이트 패턴 지정
    weak var delegate : VideoCollectionViewCellDelegate?
    
    // SUbviews
    var player : AVPlayer?
    
    private var model: VideoModel?
    
    override init(frame:CGRect){
        super.init(frame:frame)
        contentView.backgroundColor = .red
        contentView.clipsToBounds = true
        addSubviews()
    }
    
    private func addSubviews() {
        
        contentView.addSubview(videoContainer)
        
        contentView.addSubview(userNameLabel)
        contentView.addSubview(captionLabel)
        contentView.addSubview(placeNameLabel)
        contentView.addSubview(addressNameLabel)
        contentView.addSubview(reviewLabel)
        
        contentView.addSubview(likeButton)
        contentView.addSubview(shareButton)
        
        // Add actions
        likeButton.addTarget(self, action: #selector(didTapLikeButton), for: .touchDown)
        shareButton.addTarget(self, action: #selector(didTapShareButton), for: .touchDown)
        
        videoContainer.clipsToBounds = true
        
        contentView.sendSubviewToBack(videoContainer)
    }
    
    @objc private func didTapLikeButton() {
        guard let model = model else { return }
        delegate?.didTapLikeButton(with: model)
    }
    
    @objc private func didTapShareButton() {
        guard let model = model else { return }
        delegate?.didTapShareButton(with: model)
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        captionLabel.text = nil
        userNameLabel.text = nil
        placeNameLabel.text = nil
        addressNameLabel.text = nil
        reviewLabel.text = nil
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        let size = contentView.frame.size.width/6
        let width = contentView.frame.size.width
        let height = contentView.frame.size.height
        
        videoContainer.frame = contentView.bounds
        
        likeButton.frame = CGRect(x: width-size, y: height-size, width: size, height: size)
        shareButton.frame = CGRect(x: width-size, y: height-(size*2)-10, width: size, height: size)
        
    }
    
    // 여기서 with는 외부참조 변수이다.
    public func configure(with model: VideoModel){
        self.model = model
        configureVideo()
        
        //labels를 model의 값과 동일하게 맞춰준다.
        captionLabel.text = model.caption
        userNameLabel.text = model.userName
        placeNameLabel.text = model.placeName
        addressNameLabel.text = model.addressName
        reviewLabel.text = model.review
    }
    
    private func configureVideo() {
        guard let model = model else {
            return
        }
        
        guard let path = Bundle.main.path(forResource: model.videoFileName, ofType: model.videoFileFormat) else {
            print("파일을 찾지 못했어요!")
            return
        }
        // AVPlayer는 URL 객체를 넘겨줘야한다.
        player = AVPlayer(url: URL(fileURLWithPath: path))
        
        // 비디오 레이아웃을 커스텀하기 위해 사용한다.
        // AVPlaterLayer 개겣를 생성하고 player를 넣는다.
        let playerView = AVPlayerLayer()
        playerView.player = player
        playerView.frame = contentView.bounds
        // 비디오를 화면 꽉차게 만드는 것 layer의 bounds에 맞춰서 채워진다 비율도 맞게
        playerView.videoGravity = .resizeAspectFill
        videoContainer.layer.addSublayer(playerView)
        player?.volume = 0
        player?.play()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
```

### 컬렉션 뷰 셀

- 컬렉션뷰 셀은 두 개의 배경을 표시하는 뷰와 하나의 콘텐츠를 표시하는 뷰로 구성되어 있습니다. 두 개의 배경뷰는 셀이 선택되었을 때 사용자에게 시각적인 표현을 제공하기 위해 사용됩니다.
- 셀의 레이아웃은 컬렉션 뷰의 레이아웃 객체에 의해 관리된다.
- `var contentView: UIView` : 셀의 콘텐츠를 표시하는 뷰입니다.
- `var backgroundView: UIView?` : 셀의 배경을 나타내는 뷰입니다. 이 프로퍼티는 셀이 처음 로드되었을 경우와 셀이 강조 표시되지 않거나 선택되지 않을 때 항상 기본 배경의 역할을 합니다.
- `var selectedBackgroundView: UIView?` : 셀이 선택되었을 때 배경뷰 위에 표시되는 뷰입니다. 이 프로퍼티는 셀이 강조 표시되거나 선택될 때마다 기본 배경 뷰인 `backgroundView`를 대체하여 표시됩니다.





## 테이블 뷰(UITableView)

> 목록형태로 나태낼때 사용하기 좋은 메서드 이다.

- 홈 화면을 구성할때 테이블 뷰를 써야할지 콜렉션 뷰를 써야할지 너무 고민됬는데 간단한 목록 구현이라면 그냥 테이블 뷰를 사용하는게 더 좋아보인다.



## 서버연동

> RestAPI로 구성된 URL로 호출을 보내면 값을 돌려받을 수 있다.



### GET 방식

> Alamofie

- VideoCollectionViewCell



#### 문법

- let 과 var : let은 불편 프로퍼티, var는 가변 프로퍼티를 말한다.
- static이 붙게 되면 그 변수가 타입 프로퍼티라는 말이 되는데, 타입 프로퍼티는 그 구조체, 및 클래스 내부에서 사용되는 메서드를 사용할때 적용된다.
- 가끔 보다보면 클래스인데 인스턴스를 안만들고 함수를 사용하는 경우가 있는데 클래스(타입)안의 메소드가 `static func ~~` 로 되어 있으면 클래스를 그대로 가져와서 함수를 사용할 수 있다.



### extension 

> 말 그대로 확장의 의미로 뒤에 오는 컨트롤러를 상속받는 모든 클래스는 extension 아래에 지정된 메서드를 사용할 수 있다.

```swift
// UIViewContoller를 사용하는 모든 클래스에서 아래에 작성된 메서드를 호출해서 사용할 수 있다.
extension UIViewController {
    
}

```



## 동기와(sync)와 비동기(async)

```swift
     DispatchQueue.main.async {
            // Main 큐에서 비동기 방식으로 실행할 코드
        }
        DispatchQueue.global().async {
            // Background 큐에서 비동기 방식으로 실행할 코드
        }
        DispatchQueue.main.sync {
            // Main 큐에서 동기 방식으로 실행할 코드인데 이거 쓰면 오류가 발생할 확률이 있음.
        }
        DispatchQueue.global().sync {
            // Background 큐에서 동기방식으로 실행할 코드
        }
```

- `DispatchQuque.main.async` 으로 실행하는 동안 UI가 멈춰져 있게된다. (= UI상호 작용이 불가하다)  단, UI를 변경하는 코드는 Main 스레드에서 실행하도록 해야한다.

```swift
var str = ""
DispatchQueue.global().async {
  Thread.sleep(forTimeInterval: 3)
  str = "finished"
  DispatchQueue.main.async {
    self.label.text = str // UI가 변경되는 부분은 여기서 실행시켜줘야하는데, 이부분은 짦기 떄문에 오래걸리는 부분의 코드는 위에 작성하고 조금 걸리는 코드는 여기에 작성하면된다. DispatchQueue.global().async는 백그라운드에서 실행되기 떄문이다.
  }
}
```

### 구글 로그인

- 앱델리게이트 말고 viewcontroller에서 화면 전환 시키는 방법 : https://stackoverflow.com/questions/36520067/google-login-in-login-view-controller-instead-of-appdelegate-ios-swift

- 로그인 후 사용자 정보 받아올때 파라미터들

```swift
        // 사용자 정보 가져오기
        if let userId = user.userID,                  // For client-side use only!
//            let idToken = user.authentication.idToken, // Safe to send to the server
//            let fullName = user.profile.name,
//            let givenName = user.profile.givenName,
//            let familyName = user.profile.familyName,
            let email = user.profile.email {
            
            // 도대체 왜!! 와이 전체 user가 안담기냐고!!!! 주길까 일단 userId만 담자
            let plist = UserDefaults.standard
            plist.set(email, forKey: "userEmail")
            plist.synchronize()
            
        } else {
            print("Error : User Data Not Found")
        }
```

