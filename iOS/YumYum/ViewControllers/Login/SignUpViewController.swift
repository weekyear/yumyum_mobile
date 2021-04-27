//
//  SignUpViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/22.
//

import UIKit
import Alamofire

class SignUpViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var profileImgView: UIImageView!
    
    @IBOutlet weak var nickName: UITextField!
    
    @IBOutlet weak var introduction: UITextField!
    
    
    // 회원가입 완료 후 동작하는 메서드
    @IBAction func completeSignUp(_ sender: UIButton) {
        
        let param : Parameters = [
//            "email" : UserDefaults.standard.string(forKey: "userEmail")!,
            "email" : "ssafy10@a.com",
            "nickname" : self.nickName.text!,
            "introduction" : self.introduction.text!,
            "profilePath" : ""        ]
        
        // API 호출하고
        let url = URLs.signUp
        let call = AF.request(url, method: HTTPMethod.post, parameters: param, encoding: JSONEncoding.default)
        
        // 서버 응답 값을 처리해준다.
        print("버튼을 클릭했습니다!")
        call.responseJSON { res in
            let result = try! res.result.get()
            
            guard let jsonObject = result as? NSDictionary else {
                print("서버 호출 과정중에 오류!!")
                return
            }
            
            if let status = jsonObject["status"] as? String {
                if status == "200" {
                    print("성공했습니다!")
                    let userData = jsonObject["data"] as! NSDictionary
                    UserDefaults.standard.set(userData, forKey: "userData")
                    // 내용 뽑아쓸때 아래와 같이 뽑으면 서버에서 넘겨준거 그대로 쓸수있음!
                    UserDefaults.standard.dictionary(forKey: "userData")
                    
                    // 스토리 보드가 만약 분리되어 있다면 아래와 같이 스토리보드의 경로를 가져와서 변수로 할당해줘야함.
                    let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

                    if let tabbarvc = storyboard?.instantiateViewController(identifier: "MainTabBarVC") as? UITabBarController {
                        
                        tabbarvc.modalPresentationStyle = .fullScreen
                        self.present(tabbarvc, animated: true, completion: nil)
                    } else {
                        print("탭바가 없는데요?")
                    }
                }
            } else {
                self.alert("안됩니다!")
                print("되면안되는데?")
            }
            // 로그인이 성공했으면 userData에 스웨거에서 data 딕셔너리를 넣어주는 것
        }
        
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
        let headers: HTTPHeaders = [
            "Content-type": "multipart/form-data"
        ]
    
        let parameters = ["name": "name"]
        
        let call = AF.upload(multipartFormData:{ MultipartFormData in
            for (key, value) in parameters {
                MultipartFormData.append(value.data(using: String.Encoding.utf8)!, withName: key)
                } //Optional for extra parameters

            MultipartFormData.append(imgData, withName: "key", fileName: "ab.jpeg",mimeType: "image/jpeg")
        }, to: url, headers: headers)
        
        call.responseJSON{ res in
            switch res.result {
            case .success(let resut):
                print("사진업로드에 성공했습니다.: \(resut)")
            case .failure(let err):
                print("사진업로드 에러에러! \(err)")
            }
            
        }
    }
}
