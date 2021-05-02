//
//  SettingVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/01.
//

import UIKit

class SettingVC: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    
    let sectionlabel: [String] = ["계정", "컨텐츠 및 활동", "정보", "로그인"]
    let settingCellLabel : [String] = ["계정 관리", "개인정보", "푸시 알림", "다크 모드", "개인정보 보호정책", "커뮤니티 가이드라인", "로그아웃"]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
    }
}

extension SettingVC: UITableViewDelegate{
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("you tapped me!")
    }
    
}

extension SettingVC: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        
        cell.textLabel?.text = "Hello world \(indexPath.row+1)"
        cell.imageView?.image = UIImage(systemName: "bell")
        
        return cell
    }
}
