//
//  AlertUtil.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/27.
//

import UIKit

extension UIViewController {
    // alert 메서드 정의
    func alert(_ message: String, completion: (()-> Void)? = nil) {
        // 메인 스레드에서 실행되도록한다.
        DispatchQueue.main.async {
            let alert = UIAlertController(title: nil, message: message, preferredStyle: .alert)
            
            let okAction = UIAlertAction(title: "확인", style: .cancel) {(_) in
                completion?() // completion 매개 변수 값이 nil이 아닐때에만 실행되도록한다.
            }
            alert.addAction(okAction)
            self.present(alert, animated: false)
        }
    }
    // 스토리 보드 이동시 사용할 메서드
    // name은 스토리보드 파일명 : ex)Accounts, main
    // identifier는 스토리보드의 ID를 넘겨주면된다.
    func moveStoryBoard(_ name:String, _ identifier:String) {
        let storyboard: UIStoryboard? = UIStoryboard(name:name, bundle: Bundle.main)
        
        guard let movevc = storyboard?.instantiateViewController(withIdentifier: identifier) else {
            return
        }
        
        movevc.modalPresentationStyle = .fullScreen
        self.present(movevc, animated: true)
    }
    
}
