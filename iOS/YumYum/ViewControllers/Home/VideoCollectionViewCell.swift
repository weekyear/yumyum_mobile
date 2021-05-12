//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/20.
//

import UIKit
import AVFoundation

class VideoCollectionViewCell: UICollectionViewCell{
    
    @IBOutlet weak var videoLayout: UIView!
    @IBOutlet weak var foodLabel: UILabel!
    @IBOutlet weak var placeLabel: UILabel!
    @IBOutlet weak var reviewLabel: UILabel!
    @IBOutlet weak var userLabel: UILabel!
    @IBOutlet var likeCountLabel: UILabel!
    @IBOutlet var likeButton: UIButton!
    @IBOutlet var mapIcon: UIImageView!

    let yumyumYellow: ColorSet = .yumyumYellow
    
    let userData = UserDefaults.getLoginedUserInfo()!
    
    var checkLike: Bool = false
    
    var nowFeed: Feed = Feed()
    
    var player: AVPlayer?
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    func setLayout() {
        
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
        userLabel.text = "@" + (feed.user?.nickname)! as String
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

