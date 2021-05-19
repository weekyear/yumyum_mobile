//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/20.
//

import UIKit
import AVFoundation
import Lottie

class VideoCollectionViewCell: UICollectionViewCell{
    
    @IBOutlet weak var videoLayout: UIView!
    @IBOutlet weak var foodLabel: UILabel!
    @IBOutlet weak var placeLabel: UILabel!
    @IBOutlet weak var reviewLabel: UILabel!
    @IBOutlet weak var userBtn: UIButton!
    @IBOutlet var likeCountLabel: UILabel!
    @IBOutlet var likeButton: UIButton!
    @IBOutlet var mapIcon: UIImageView!
    
    var index : Int = 0
    var delegate: userProfileBtnDelegate?

    @IBOutlet weak var scoreOneView: UIView!
    @IBOutlet weak var scoreTwoView: UIView!
    @IBOutlet weak var scoreThreeView: AnimationView!
    @IBOutlet weak var scoreFourView: AnimationView!
    @IBOutlet weak var scoreFiveView: AnimationView!
    
    let animationview = AnimationView(name: "ic_vomited")
    let animationview2 = AnimationView(name: "ic_confused")
    let animationview3 = AnimationView(name: "ic_neutral")
    let animationview4 = AnimationView(name: "ic_lol")
    let animationview5 = AnimationView(name: "ic_inloveface")

    let yumyumYellow: ColorSet = .yumyumYellow
    
    let userData = UserDefaults.getLoginedUserInfo()!
    
    var checkLike: Bool = false
    
    var nowFeed: Feed = Feed()
    
    var player: AVPlayer?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setUpAnimation()
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        let keyPath = AnimationKeypath(keypath: "**.Stroke 1.Color")
        let keyPathEyes = AnimationKeypath(keypath: "**.Fill 1.Color")
        let black = Color(r: (0/255), g: (0/255), b: (0/255), a: 1)
        let blackColorValueProvider = ColorValueProvider(black)
        animationview.stop()
        animationview2.stop()
        animationview3.stop()
        animationview4.stop()
        animationview5.stop()
        
        animationview.setValueProvider(blackColorValueProvider, keypath: keyPath)
        animationview.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
        animationview2.setValueProvider(blackColorValueProvider, keypath: keyPath)
        animationview2.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
        animationview3.setValueProvider(blackColorValueProvider, keypath: keyPath)
        animationview3.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
        animationview4.setValueProvider(blackColorValueProvider, keypath: keyPath)
        animationview4.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
        animationview5.setValueProvider(blackColorValueProvider, keypath: keyPath)
        animationview5.setValueProvider(blackColorValueProvider, keypath: keyPathEyes)
        
        animationview.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview2.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview3.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview4.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview5.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
    }
    
    public func setUpAnimation() {
        animationview.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview.contentMode = .scaleAspectFit
        scoreOneView.addSubview(animationview)

        animationview2.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview2.contentMode = .scaleAspectFit
        scoreTwoView.addSubview(animationview2)
        
        animationview3.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview3.contentMode = .scaleAspectFit
        scoreThreeView.addSubview(animationview3)
        
        animationview4.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview4.contentMode = .scaleAspectFit
        scoreFourView.addSubview(animationview4)

        animationview5.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        animationview5.contentMode = .scaleAspectFit
        scoreFiveView.addSubview(animationview5)
    }
    
    @IBAction func userBtnPress(_ sender: Any) {
        self.delegate?.userBtnPress(index: index, nowfeed: nowFeed)
    }
    
    @IBAction func likeBtnPress(_ sender: Any) {
        if checkLike == true {
            checkLike = false
            likeButton.tintColor = .white
            let userId = userData["id"].intValue
            let feedId = nowFeed.id!
            
            WebApiManager.shared.cancleLikeFeed(feedId: feedId, userId: userId){ (result) in
                if result["status"] == "200"{
                    print("싫어요요청을 보냈습니다.")
                }
            } failure: { (error) in
                print(error.localizedDescription)
                print("좋아요 서버 호출 에러")
            }
            likeCountLabel.text = String(Int(likeCountLabel.text!)! - 1)
        } else {
            checkLike = true
            likeButton.tintColor = yumyumYellow.toColor()
            
            var likeInfo = userLike()
            likeInfo.userId = userData["id"].intValue
            likeInfo.feedId = nowFeed.id!
            
            WebApiManager.shared.postLikeFeed(likeInfo: likeInfo){ (result) in
                if result["status"] == "200"{
                    print("좋아요 포스트 요청을 보냈습니다.")
                }
            } failure: { (error) in
                print(error.localizedDescription)
                print("좋아요 서버 호출 에러")
            }
            likeCountLabel.text = String(Int(likeCountLabel.text!)! + 1)
        }
    }
    
    public func configureVideo(with feed:Feed, myLikeFeed:Feed) {
        self.player = AVPlayer(url: feed.videoPath!)
        NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidReachEnd), name: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: self.player?.currentItem)
        let playerView = AVPlayerLayer()
        playerView.player = self.player
        playerView.frame = videoLayout.bounds
        playerView.videoGravity = .resize
        videoLayout.layer.addSublayer(playerView)
        self.player!.volume = 0
        self.player?.play()
        loadData(feed: feed, myLikeFeed: myLikeFeed)
    }
    
    @objc func playerItemDidReachEnd(notification: NSNotification) {
        self.player?.seek(to: CMTime.zero)
        self.player?.play()
    }
    
    private func loadData(feed:Feed, myLikeFeed: Feed) {
        nowFeed = feed
        foodLabel.text = feed.title
        userBtn.setTitle("@" + (feed.user?.nickname!)! , for: .normal)
        reviewLabel.text = feed.content
        
        
        if let arr = feed.place?.address.components(separatedBy: " ") {
            if arr.count > 1 {
                placeLabel.text = String(feed.place?.name ?? " ") +
                    " | \(String(arr[0])) \(String(arr[1]))"
                likeCountLabel.text = String(feed.likeCount!)
                
            }
        }
        
        checkLike = myLikeFeed.isLike ?? false
        
        if checkLike == true {
            likeButton.tintColor = yumyumYellow.toColor()
        } else {
            likeButton.tintColor = .white
        }
        
    }
    
}

protocol userProfileBtnDelegate {
    func userBtnPress(index: Int, nowfeed : Feed)
}
