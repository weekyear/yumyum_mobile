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