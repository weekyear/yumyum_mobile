//
//  ReviewVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/29.
//

import UIKit
import SwiftyJSON
import Lottie

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
    
    
    @IBOutlet var scoreOneView: AnimationView!
    @IBOutlet var scoreTwoView: AnimationView!
    @IBOutlet var scoreThreeView: AnimationView!
    @IBOutlet var scoreFourView: AnimationView!
    @IBOutlet var scoreFiveView: AnimationView!
    
    let animationview = AnimationView(name: "ic_vomited")
    let animationview2 = AnimationView(name: "ic_confused")
    let animationview3 = AnimationView(name: "ic_neutral")
    let animationview4 = AnimationView(name: "ic_lol")
    let animationview5 = AnimationView(name: "ic_inloveface")
    
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var contentTextField: UITextField!
    @IBOutlet var locationTextField: UITextField!
    @IBOutlet weak var emojiLabel: UILabel!
    
    @IBAction func didTabLocationButton(_ sender: Any) {
        let vc = PlaceSearchVC.instance()
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setUpAnimation()
        setAnimationGesture()
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
    
    func setUpAnimation() {
        
        animationview.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview.contentMode = .scaleAspectFit
        scoreOneView.addSubview(animationview)
    
        animationview2.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview2.contentMode = .scaleAspectFit
        scoreTwoView.addSubview(animationview2)
        
        animationview3.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview3.contentMode = .scaleAspectFit
        scoreThreeView.addSubview(animationview3)
        
        animationview4.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview4.contentMode = .scaleAspectFit
        scoreFourView.addSubview(animationview4)

        animationview5.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
        animationview5.contentMode = .scaleAspectFit
        scoreFiveView.addSubview(animationview5)

    }
    
    func setAnimationGesture() {
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture2 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture3 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture4 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        
        let tapGesture5 = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
        animationview.tag = 1
        animationview2.tag = 2
        animationview3.tag = 3
        animationview4.tag = 4
        animationview5.tag = 5
        animationview.addGestureRecognizer(tapGesture)
        animationview2.addGestureRecognizer(tapGesture2)
        animationview3.addGestureRecognizer(tapGesture3)
        animationview4.addGestureRecognizer(tapGesture4)
        animationview5.addGestureRecognizer(tapGesture5)
        
    }
    
    @objc func handleTap(sender: UITapGestureRecognizer) {
        let fillKeypath = AnimationKeypath(keypath: "**.**.Color" )
        let redValueProvider = ColorValueProvider(Color(r: 1, g: 0.2, b: 0.3, a: 1))
        
        
        switch sender.view?.tag {
        case 1:
            animationview.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview.setValueProvider(redValueProvider, keypath: fillKeypath)
            animationview.play()
            animationview.loopMode = .loop
            animationview2.stop()
            animationview3.stop()
            animationview4.stop()
            animationview5.stop()
            setEmoji(value: .one)
        case 2:
            animationview2.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview2.setValueProvider(redValueProvider, keypath: fillKeypath)
            animationview.stop()
            animationview3.stop()
            animationview4.stop()
            animationview5.stop()
            animationview2.play()
            animationview2.loopMode = .loop
            setEmoji(value: .two)
        case 3:
            animationview3.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview3.setValueProvider(redValueProvider, keypath: fillKeypath)
            animationview3.loopMode = .loop
            animationview2.stop()
            animationview.stop()
            animationview4.stop()
            animationview5.stop()
            animationview3.play()
            setEmoji(value: .three)
        case 4:
            animationview4.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview4.setValueProvider(redValueProvider, keypath: fillKeypath)
            animationview4.loopMode = .loop
            animationview4.play()
            animationview2.stop()
            animationview3.stop()
            animationview.stop()
            animationview5.stop()
            setEmoji(value: .four)
        case 5:
            animationview5.frame = CGRect(x: 0, y: 0, width: 45, height: 45)
            animationview5.setValueProvider(redValueProvider, keypath: fillKeypath)
            animationview5.play()
            animationview5.loopMode = .loop
            animationview2.stop()
            animationview3.stop()
            animationview4.stop()
            animationview.stop()
            setEmoji(value: .five)
        default:
            break
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
                self.feed.content = self.contentTextField.text
                self.feed.title = self.titleTextField.text
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
