//
//  LoginViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//


import UIKit
import AuthenticationServices
import GoogleSignIn

class LoginViewController : UIViewController {
    
    @IBOutlet weak var signInButton : GIDSignInButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        GIDSignIn.sharedInstance()?.presentingViewController = self
         // Automatically sign in the user.
        GIDSignIn.sharedInstance()?.restorePreviousSignIn()
         // ...
        signInButton.style = .wide
        addButton()
    }

    @IBOutlet var LoginView: UIView!

    // 애플 로그인 버튼을 구현할 메서드 추가
    func addButton() {
        let button = ASAuthorizationAppleIDButton(authorizationButtonType: .signIn, authorizationButtonStyle: .black)

        button.addTarget(self, action: #selector(handleAuthorizationAppleIDButtonPress), for: .touchUpInside)

        button.translatesAutoresizingMaskIntoConstraints = false

        LoginView.addSubview(button)
        button.centerXAnchor.constraint(equalTo:LoginView.centerXAnchor)
                .isActive = true
        button.centerYAnchor.constraint(equalTo:LoginView.centerYAnchor)
                .isActive = true
        button.heightAnchor.constraint(equalTo: LoginView.heightAnchor, multiplier: 0.6)
                    .isActive = true
        button.widthAnchor.constraint(equalTo: LoginView.widthAnchor)
                    .isActive = true

    }

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

    // Apple ID 연동 성공 시
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

    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        // Handle error.
    }


}
