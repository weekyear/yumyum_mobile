//
//  SignUpViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/22.
//

import UIKit

class SignUpViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet var profileImgView: UIImageView!
    
    @IBAction func completeSignUp(_ sender: UIButton) {
        print("출력!")
        // 스토리 보드가 만약 분리되어 있다면 아래와 같이 스토리보드의 경로를 가져와서 변수로 할당해줘야함.
        let storyboard: UIStoryboard? = UIStoryboard(name: "Main", bundle: Bundle.main)

        if let tabbarvc = storyboard?.instantiateViewController(identifier: "TabBarContoller") as? UITabBarController {
            
            tabbarvc.modalPresentationStyle = .fullScreen
            self.present(tabbarvc, animated: true, completion: nil)
        } else {
            print("탭바가 없는데요?")
        }
    }
    
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
        }
        
        picker.dismiss(animated: true)
    }
}
