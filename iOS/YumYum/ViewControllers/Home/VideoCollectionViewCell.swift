//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/20.
//

import UIKit
import AVFoundation
import GoogleSignIn

class VideoCollectionViewCell: UICollectionViewCell{
    
    @IBOutlet weak var videoLayout: UIView!
    
    @IBOutlet weak var foodLabel: UILabel!
    
    @IBOutlet weak var placeLabel: UILabel!
    
    @IBOutlet weak var addressLabel: UILabel!
    
    @IBOutlet weak var reviewLabel: UILabel!
    
    @IBOutlet weak var userLabel: UILabel!
    
    @IBOutlet weak var placeStackView : UIStackView!
    
    @IBOutlet var likeCountLabel: UILabel!
    
    @IBOutlet var Likebutton: UIButton!
    
//    var player : AVPlayer?
    
    let userData = UserDefaults.getLoginedUserInfo()!
    
    var checkLike: Bool = false
    
    var nowFeed : Feed = Feed()
    
    var player: AVPlayer?
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    @IBAction func likeBtnPress(_ sender: Any) {
        if checkLike == true {
            checkLike = false
            let image = UIImage(named: "ic_thumbs_up")
            Likebutton.setImage(image, for: .normal)
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
            let image = UIImage(named: "ic_thumbs_up_filled")
            Likebutton.setImage(image, for: .normal)
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
        bringUpViewobject()
    }
    
    @objc func playerItemDidReachEnd(notification: NSNotification) {
        self.player?.seek(to: CMTime.zero)
        self.player?.play()
    }
    
    private func loadData(feed:Feed, myLikeFeed: Feed) {
        nowFeed = feed
        foodLabel.text = feed.title
        userLabel.text = feed.user?.nickname
        reviewLabel.text = feed.content
        placeLabel.text = feed.place?.name
        addressLabel.text = feed.place?.address
        likeCountLabel.text = String(feed.likeCount!)
        checkLike = myLikeFeed.isLike ?? false
        
        if checkLike == true {
            let image = UIImage(named: "ic_thumbs_up_filled")
            Likebutton.setImage(image, for: .normal)
        } else {
            let image = UIImage(named: "ic_thumbs_up")
            Likebutton.setImage(image, for: .normal)
        }
        
    }
    
    private func bringUpViewobject() {
        videoLayout.bringSubviewToFront(foodLabel)
        videoLayout.bringSubviewToFront(placeLabel)
        videoLayout.bringSubviewToFront(addressLabel)
        videoLayout.bringSubviewToFront(reviewLabel)
        videoLayout.bringSubviewToFront(userLabel)
        videoLayout.bringSubviewToFront(placeStackView)
        videoLayout.bringSubviewToFront(Likebutton)
        videoLayout
            .bringSubviewToFront(likeCountLabel)
    }
}

