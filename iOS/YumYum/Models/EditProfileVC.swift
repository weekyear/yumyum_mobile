//
//  EditProfileVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/03.
//

import UIKit

class EditProfileVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var profileImgView: UIImageView!
    
    @IBOutlet weak var nickNameTF: UITextField!
    
    @IBOutlet weak var introduceTF: UITextField!
    
    var user = User(profilePath: "", nickname: "", introduction: "", id: 0)
    
    
    @IBAction func completeBtn(_ sender: Any) {
        let userData = UserDefaults.getLoginedUserInfo()
        user.id = userData!["id"].intValue
        user.nickname = self.nickNameTF.text
        user.introduction = self.introduceTF.text
        
        WebApiManager.shared.updateProfile(user: user) { (result) in
            print(result)
        } failure: { (error) in
            print(error)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let user = UserDefaults.getLoginedUserInfo()
        // 인전값을 그대로 가져와서 회원변경 탭에 넣어준다.
        nickNameTF.text = user!["nickname"].stringValue
        introduceTF.text = user!["introduction"].stringValue
        if let url = URL(string: user!["profilePath"].stringValue) {
            var image: UIImage?
            
            DispatchQueue.global().async {
                let data = try? Data(contentsOf: url)
                DispatchQueue.main.async {
                    image = UIImage(data: data!)
                    self.profileImgView.image = image
                }
            }
        } else {
            var image: UIImage?
            image = UIImage(systemName: "person.crop.circle.badge.plus")
            self.profileImgView.image = image
        }
        imageMakeRouded(imageview: profileImgView)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(touchToPickPhoto))
        
        profileImgView.addGestureRecognizer(tapGesture)
        profileImgView.isUserInteractionEnabled = true
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
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let img = info[UIImagePickerController.InfoKey.editedImage] as? UIImage {
            self.profileImgView.image = img
            WebApiManager.shared.createProfilePath(image: img) {
                (result) in
                self.user.profilePath = result["data"].stringValue
            } failure: { (error) in
                print(error)
            }
        }

        picker.dismiss(animated: true)
    }
    
}
