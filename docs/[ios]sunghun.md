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

- 이런 이유들로 메인페이지에서 동영상을 보여줄떄 `UIcollectionView`를 쓰는게 맞아 보임.



```swift

```

### 접근제어

> 코드끼리 상호작용할 때 파일 간 또는 모듈 간에 접근을 제한할 수 있는 기능이다. 접근 제어를 통해 코드의 상세 구현은 숨기고 허용된 기능만 사용하는 인터페이스를 제공할 수 있다.

- public :  자신이 구현된 소스 파일 물론, 그 소스파일이 속해 있는 모듈, 그 모듈을 가져다 쓴느 모듈 등 모든 곳에서 사용할 수 있다. 주로 외부와 연결될 인터페이스 구현을 하는데 많이 쓰인다. Model 파일을 public으로 지정해서 쓰기도함.
- private :  접근 수준이 가장 한정적인 범위, 소스파일 내부에서만 사용할 수있으며, 심지어 같은 파일안에 구현한 다른 타입이나 기능에서도 사용할 수가 없다. 