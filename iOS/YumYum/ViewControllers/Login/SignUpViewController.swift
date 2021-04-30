//
//  SignUpViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/22.
//

import UIKit
import Alamofire
import SwiftyJSON

class SignUpViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var profileImgView: UIImageView!
    
    @IBOutlet weak var nickNameLabel: UITextField!
    
    @IBOutlet weak var introductionLabel: UITextField!
    
    var profilePath : String? = ""

    
    // 회원가입 완료 후 동작하는 메서드
    @IBAction func completeSignUp(_ sender: UIButton) {
        // 모델을 만들어서 여기에 내용을 담고 그걸 가져다 쓰는 로직이 좋다.
        let userInfo = UserModel()
        userInfo.userEmail = UserDefaults.standard.string(forKey: "USEREMAIL")!
        userInfo.nickName = self.nickNameLabel.text!
        userInfo.introduce = self.introductionLabel.text!
        userInfo.profileImg = self.profilePath!
        
        WebApiManager.shared.userSignUp(userData: userInfo, successHandler: { (data) in
            UserDefaults.saveLoginedUserInfo(data)
            
            let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

            if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {

                tabbarvc.modalPresentationStyle = .fullScreen
                self.present(tabbarvc, animated: true, completion: nil)
            } else {
                print("탭바가 없습니다.")
            }
            
        }, failureHandler: {(error) in
            print(error)
        })
    }
    
    
    // 회원가입 둥글게 만들기!
    func makeRounded() {
        profileImgView.layer.borderWidth = 2
        profileImgView.layer.masksToBounds = false
        profileImgView.layer.borderColor = UIColor.systemYellow.cgColor
        profileImgView.layer.cornerRadius = profileImgView.frame.height/2 //This will change with corners of image and height/2 will make this circle shape
        profileImgView.clipsToBounds = true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(touchToPickPhoto))
        
        profileImgView.addGestureRecognizer(tapGesture)
        profileImgView.isUserInteractionEnabled = true
        self.introductionLabel.placeholder = "한줄 소개를 입력해주세요."
        self.introductionLabel.autocorrectionType = .no
        self.introductionLabel.autocapitalizationType = .none
        self.nickNameLabel.placeholder = "별명을 입력해주세요."
        self.nickNameLabel.autocorrectionType = .no
        self.nickNameLabel.autocapitalizationType = .none
        
        makeRounded()
        
    }
    
    @objc func touchToPickPhoto(){
        let alert = UIAlertController(title: nil, message: "사진가져올 곳을 선택해주세요.", preferredStyle: .actionSheet)
        
        // 저장된 앨범을 사용할 수 있으면
        if UIImagePickerController.isSourceTypeAvailable(.savedPhotosAlbum) {
            alert.addAction(UIAlertAction(title: "저장된 앨범", style: .default){ (_) in self.imgPikcer(.savedPhotosAlbum)
            })
        }
        
        // 포토 라이브러리를 사용할 수 있으면 클로저 문법으로 뒤의 함수를 실행한다.
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            alert.addAction(UIAlertAction(title: "포토 라이브러리", style: .default){ (_) in self.imgPikcer(.photoLibrary)
            })
        }
        // 취소 버튼 추가
        alert.addAction(UIAlertAction(title: "취소", style: .cancel, handler: nil))
        
        self.present(alert, animated: true)
        
    }
    // 포토라이브러리랑 저장앨범을 구분해서 알람창을 띄우기 위해! souce를 다르게 받아온다.
    func imgPikcer (_ source : UIImagePickerController.SourceType){
        let picker = UIImagePickerController()
        picker.sourceType = source
        picker.delegate = self
        picker.allowsEditing = true
        self.present(picker, animated: true)
    }
    
    // 이미지를 선택하면 이 메소드가 자동으로 출력된다.
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let img = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
            self.profileImgView.image = img
            getPicturePath(img)
        }
        
        picker.dismiss(animated: true)
    }
    
    func getPicturePath(_ image: UIImage) {
        let url = URLs.profile
        let imgData = image.jpegData(compressionQuality: 0.2)!
        let headers: HTTPHeaders
        headers = [
            "Content-type": "multipart/form-data",
            "Content-Disposition" : "form-data",
        ]
    
        let parameters = ["name": "name"]
        
        let call = AF.upload(multipartFormData:{ multipartFormData in
            for (key, value) in parameters {
                multipartFormData.append(value.data(using: .utf8)!, withName: key)
                } //Optional for extra parameters
            multipartFormData.append(imgData, withName: "file", fileName: "file.jpeg",mimeType: "image/jpeg")
        }, to: url, method: .post, headers: headers)
        
        call.responseJSON{ res in
            switch res.result {
            case .success(_):
                print("성공했습니다.")
                let result = try! res.result.get()
                
                guard let jsonObject = result as? NSDictionary
                else {
                    print("타입이 잘못되었어요!")
                    return
                }
                
                if let data = jsonObject["data"] as? String {
                    self.profilePath = data
                }
                
            case .failure(let err):
                print("사진업로드 에러에러! \(err)")
            }
        }
    }
}
