//
//  ReviewVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import UIKit
import SwiftyJSON

enum Score: Int {
    case one = 1
    case two = 2
    case three = 3
    case four = 4
    case five = 5
}

class ReviewVC: UIViewController {
    
    static func instance(videoUrl: URL) -> ReviewVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "ReviewVC") as! ReviewVC
        vc.videoUrl = videoUrl
        return vc
    }
    
    var videoUrl: URL!
    var feed: Feed = Feed()
    var score: Score = .five
    var place: Place?
    var tempfeed: Feed?
    
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var contentTextField: UITextField!
    @IBOutlet var locationTextField: UITextField!
    @IBOutlet weak var emojiLabel: UILabel!
    
    @IBAction func TabEmoji1(_ sender: Any) {
        setEmoji(value: .one)
    }
    @IBAction func TabEmoji2(_ sender: Any) {
        setEmoji(value: .two)
    }
    @IBAction func TabEmoji3(_ sender: Any) {
        setEmoji(value: .three)
    }
    @IBAction func TabEmoji4(_ sender: Any) {
        setEmoji(value: .four)
    }
    @IBAction func TabEmoji5(_ sender: Any) {
        setEmoji(value: .five)
    }
    
    @IBAction func didTabLocationButton(_ sender: Any) {
        let vc = PlaceSearchVC.instance()
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setLayout()
        titleTextField.delegate = self
        contentTextField.delegate = self
        titleTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .editingChanged)
        contentTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .editingChanged)
        locationTextField.addTarget(self, action: #selector(textFieldDidChange(_sender:)), for: .valueChanged)
        
        if tempfeed != nil {
            loadTempData()
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        checkNowReviewData()
    }
    
    func checkNowReviewData() {
        if titleTextField.text == "" || contentTextField.text == "" || locationTextField.text == "" || emojiLabel.text == "Label"{
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
        } else {
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
            self.feed.isCompleted = true
        }
    }
    
    private func loadTempData() {
        feed = tempfeed!
        feed.userId = tempfeed?.user?.id
        setEmoji(value: Score(rawValue: feed.score!)!)
        titleTextField.text = feed.title
        contentTextField.text = feed.content
        locationTextField.text = feed.place?.name
    }
    
    @objc func textFieldDidChange(_sender: UITextField) {
        if titleTextField.text == "" || contentTextField.text == "" || locationTextField.text == "" || emojiLabel.text == "Label"{
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
        } else {
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
            self.feed.isCompleted = true
        }
    }
    
    func setLayout() {
        // navigationBar
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.title = "리뷰쓰기"
        self.navigationItem.leftBarButtonItem = UIBarButtonItem(title: "취소", style: .plain, target: self, action: #selector(leftBarButtonAction))
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(title: "임시저장", style: .plain, target: self, action: #selector(rightBarButtonAction))
    }
    
    @objc
    func leftBarButtonAction() {
        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }
    
    @objc
    func rightBarButtonAction() {
        if self.videoUrl != nil {
            WebApiManager.shared.createMediaPath(mediaUrl: self.videoUrl) { (result) in
                print("json: \(result)")
                self.feed.thumbnailPath = URL(string: result["data"]["thumbnailPath"].stringValue)
                self.feed.videoPath = URL(string: result["data"]["videoPath"].stringValue)
                let user = UserDefaults.getLoginedUserInfo()
                self.feed.userId = user!["id"].intValue
                
                self.feed.score = self.score.rawValue
                
                self.feed.place = self.place
                
                WebApiManager.shared.createFeed(feed: self.feed) { (result) in
                    print("feed생성: \(result)")
                    if result["status"] == "200" {
                        self.dismiss(animated: true, completion: nil)
                    }

                } failure: { (error) in
                    print("feed생성에러: \(error)")
                }
            } failure: { (error) in
                print("error: \(error)")
            }
        } else {
            print(self.feed)
            print("임시저장시 들아오는 피드 생성부분")
            WebApiManager.shared.createFeed(feed: self.feed) { (result) in
                print("feed생성: \(result)")
                if result["status"] == "200" {
                    self.navigationController?.popViewController(animated: true)
                }
            } failure: { (error) in
                print("feed생성에러: \(error)")
            }
        }
    }

    func setEmoji(value: Score) {
        self.score = value
        switch value {
        case .one:
            emojiLabel.text = "웩맛"
        case .two:
            emojiLabel.text = "노맛"
        case .three:
            emojiLabel.text = "중맛"
        case .four:
            emojiLabel.text = "맛맛"
        case .five:
            emojiLabel.text = "쫀맛"
        }
    }

}


extension ReviewVC: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == titleTextField {
            self.feed.title = titleTextField.text ?? ""

        } else if textField == contentTextField {
            self.feed.content = contentTextField.text ?? ""
        }
    }
}


extension ReviewVC: PlaceDelegate {
    func setPlace(_ controller: PlaceSearchVC, place: Place) {
        print("띵동place도착: \(place)")
        self.locationTextField.text = place.name
        self.place = place
        self.feed.place = place
    }
}
