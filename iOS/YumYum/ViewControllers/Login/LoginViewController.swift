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
import Alamofire

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
//        checkLogin()
        // 구글 로그인되어있는지 안되어 있는지 확인
        print(GIDSignIn.sharedInstance()?.currentUser != nil)

        UserDefaults.standard.removeObject(forKey: "USEREMAIL")
        UserDefaults.standard.removeObject(forKey: "USERDATA")
//        WebApiManager.shared.userLogin()
        GIDSignIn.sharedInstance()?.signOut()
    }
    
    func checkLogin(){
        print("들어오나요?")
        if plist.string(forKey: "userEmail") != nil {
            let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

            if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {
                
                tabbarvc.modalPresentationStyle = .fullScreen
                self.present(tabbarvc, animated: true, completion: nil)
            } else {
                print("로그인이 안되어있어요!")
            }
        }
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
        button.heightAnchor.constraint(equalTo: appleLoginView.heightAnchor, multiplier: 0.9)
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
            // 구글 로그인이 완료되었을떄 아래 실행
            } else {
            guard let userEmail = user.profile.email else {
                return
            }
                
            //MARK: - 리턴 받기
            // 이부분 아래가 실행되고 응답을 받는데 아래 로직이 실행되고
            WebApiManager.shared.userLogin(userEmail, successHandler: { (data) in
                // 서버에서 정상적으로 처리가 되면 해당 블록이 처리 됩니다.
                if userEmail == data["email"] as? String {
                    // 로그인 이메일과 서버이메일이 같으면 데이터를 UserDefaults에 저장하고 스토리보드를 이동시킨다.
                    UserDefaults.saveLoginedUserInfo(data)
                    let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

                    if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {

                        tabbarvc.modalPresentationStyle = .fullScreen
                        self.present(tabbarvc, animated: true, completion: nil)
                    } else {
                        print("탭바가 없습니다.")
                    }
                    
                }
            }, failureHandler: { (error) in
                //서버에 유저 정보가 저장되어 있지 않다는 것이기 때문에 
                print(error)
                self.moveStoryBoard("Accounts", "SignUpViewController")
            })
                
            // 사용자 정보 가져와서 저장하기
            if let email = user.profile.email {
                UserDefaults.saveLoginUserEmail(email)
            } else {
                print("Error : User Data Not Found")
            }
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
    }
        
    // 로그아웃되면 실행되는 함수
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!,
              withError error: Error!) {
      // Perform any operations when the user disconnects from app here.
      // ...
        print("로그아웃되었어요!")
    }
}
