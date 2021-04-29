//
//  AlertUtil.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/27.
//

import UIKit

extension UIViewController {
    // MARK: - 알람 메서드
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
    
    // MARK: - 스토리보드 이동
    func moveStoryBoard(_ name:String, _ identifier:String) {
        let storyboard: UIStoryboard? = UIStoryboard(name:name, bundle: Bundle.main)
        
        guard let movevc = storyboard?.instantiateViewController(withIdentifier: identifier) else {
            return
        }
        
        movevc.modalPresentationStyle = .fullScreen
        self.present(movevc, animated: true)
    }
}
