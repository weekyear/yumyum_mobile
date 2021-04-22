//
//  LoginViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//


import UIKit
import AuthenticationServices
import GoogleSignIn
import Firebase

class LoginViewController : UIViewController, GIDSignInDelegate {
    @IBOutlet weak var googleLoginView : GIDSignInButton!
    @IBOutlet var appleLoginView: UIView!
    
    let plist = UserDefaults.standard

    override func viewDidLoad() {
        super.viewDidLoad()
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance()?.presentingViewController = self
         // 자동로그인을 설정헌다.
        GIDSignIn.sharedInstance()?.restorePreviousSignIn()
         // 버튼 스타일지정
        googleLoginView.style = .wide
        addButton()
        checkLogin()
        // view클릭하면 이동시키도록 지정
//        clickView()
    }
    
    func checkLogin(){
        if let userId = plist.string(forKey: "userInfo"){
            
        }
    }
    // 클릭했을때 회원가입 화면으로 넘어가도록 지정 근대 이렇게하니까 바로 넘어가버리네..
//    func clickView() {
//        self.googleLoginView.addTarget(self, action: #selector(moveSignUp(_:)), for: UIControl.Event.touchDown)
//    }
    
    @objc func moveSignUp(_ sender: Any) {
        let storyboard: UIStoryboard? = UIStoryboard(name: "Accounts", bundle: Bundle.main)
        
        guard let signupvc = storyboard?.instantiateViewController(withIdentifier: "SignUpViewController") else {
            return
        }
        
        signupvc.modalPresentationStyle = .fullScreen
        self.present(signupvc, animated: true)
    }
    
    // 애플 로그인 버튼을 구현할 메서드 추가
    func addButton() {
        let button = ASAuthorizationAppleIDButton(authorizationButtonType: .signIn, authorizationButtonStyle: .black)

        button.addTarget(self, action: #selector(handleAuthorizationAppleIDButtonPress), for: .touchUpInside)

        button.translatesAutoresizingMaskIntoConstraints = false

        appleLoginView.addSubview(button)
        button.centerXAnchor.constraint(equalTo:appleLoginView.centerXAnchor)
                .isActive = true
        button.centerYAnchor.constraint(equalTo:appleLoginView.centerYAnchor)
                .isActive = true
        button.heightAnchor.constraint(equalTo: appleLoginView.heightAnchor, multiplier: 0.6)
                    .isActive = true
        button.widthAnchor.constraint(equalTo: appleLoginView.widthAnchor)
                    .isActive = true

    }
    
    // 애플 로그인 버튼을 눌면 실행되는 메서드
    @objc
    func handleAuthorizationAppleIDButtonPress() {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]
        let controller = ASAuthorizationController(authorizationRequests: [request])
        controller.delegate = self as? ASAuthorizationControllerDelegate
        controller.presentationContextProvider = self as? ASAuthorizationControllerPresentationContextProviding
        controller.performRequests()
    }

    // 애플로그인 버튼 클릭시 모달 띄우기
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.view.window!
    }

    // Apple ID 연동 성공 시 실행되는 메서드
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        switch authorization.credential {
        // Apple ID
        case let appleIDCredential as ASAuthorizationAppleIDCredential:

            // 계정 정보 가져오기
            let userIdentifier = appleIDCredential.user
            let fullName = appleIDCredential.fullName
            let email = appleIDCredential.email

            print("User ID : \(userIdentifier)")
            print("User Email : \(email ?? "")")
            print("User Name : \((fullName?.givenName ?? "") + (fullName?.familyName ?? ""))")

        default:
            break
        }
    }
    // 인증 실패시 아래 함수 호출
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        // Handle error.
    }
    
    // 구글 로그인을 완료했을때 불려져 오는 메서드이다.
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!,
              withError error: Error!) {
          if let error = error {
            if (error as NSError).code == GIDSignInErrorCode.hasNoAuthInKeychain.rawValue {
              print("The user has not signed in before or they have since signed out.")
            } else {
              print("\(error.localizedDescription)")
            }
            return
            // 로그인 완료됬을떄 아래 실행
          } else {
            let storyboard: UIStoryboard? = UIStoryboard(name: "Accounts", bundle: Bundle.main)
            
            guard let signupvc = storyboard?.instantiateViewController(withIdentifier: "SignUpViewController") else {
                return
            }
            
            signupvc.modalPresentationStyle = .fullScreen
            self.present(signupvc, animated: true)
          }
        // 로그인 완료 했을때 firebase에 저장하기
        guard let authentication = user.authentication else {return}
        
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken, accessToken: authentication.accessToken)
        
        Auth.auth().signIn(with: credential, completion: {(user, error) in
            if let error = error {
                    print(error)
                }
            }
        )
        // 사용자 정보 가져오기
        if let userId = user.userID,                  // For client-side use only!
            let idToken = user.authentication.idToken, // Safe to send to the server
            let fullName = user.profile.name,
            let givenName = user.profile.givenName,
            let familyName = user.profile.familyName,
            let email = user.profile.email {
            
            // 도대체 왜!! 와이 전체 user가 안담기냐고!!!! 주길까 일단 UserId만 담자
            let plist = UserDefaults.standard
            plist.set(email, forKey: "userInfo")
            plist.synchronize()
            
//            moveSignUp()
            
        } else {
            print("Error : User Data Not Found")
        }
        
    }
    
    // 로그아웃되면 실행되는 함수
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
      // Perform any operations when the user disconnects from app here.
      // ...
        print("로그아웃되었어요!")
    }



}
