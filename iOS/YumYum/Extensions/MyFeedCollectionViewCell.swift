//
//  MyFeedCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/11.
//

import UIKit
import AVFoundation

class MyFeedCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var myVideoView : UIView!
    @IBOutlet weak var myNameLabel: UILabel!
    @IBOutlet weak var myFoodLabel: UILabel!
    @IBOutlet weak var myPlaceLabel: UILabel!
    @IBOutlet weak var myReviewLabel: UILabel!
    @IBOutlet var myLikeCountLabel: UILabel!
    @IBOutlet var myLikeBtn: UIButton!
    @IBOutlet var mapIcon: UIImageView!
    
    let yumyumYellow: ColorSet = .yumyumYellow
    
    var player: AVPlayer?
    var feedInfo : Feed = Feed()
    
    var checkLike: Bool = false
    
    @IBOutlet var backMyPageBtn: UIButton!
    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    public func configureVideo(with feed:Feed) {
        self.feedInfo = feed
        self.player = AVPlayer(url: feed.videoPath!)
        NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidReachEnd), name: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: self.player?.currentItem)
        let playerView = AVPlayerLayer()
        playerView.player = self.player
        playerView.frame = myVideoView.bounds
        playerView.videoGravity = .resize
        myVideoView.layer.addSublayer(playerView)
        self.player!.volume = 0
        self.player?.play()
        loadData(feed: feed)
    }
    
    @objc func playerItemDidReachEnd(notification: NSNotification) {
        self.player?.seek(to: CMTime.zero)
        self.player?.play()
    }
    
    private func loadData(feed:Feed) {
        myFoodLabel.text = feed.title
        myNameLabel.text = "@" + (feed.user?.nickname)! as String
        myReviewLabel.text = feed.content
        
        let arr = feed.place?.address.components(separatedBy: " ")
        
//        placeLabel.text = String(feed.place?.name ?? " ") + " | \(String(arr?[0] ?? " ")) \(String(arr?[1] ?? " "))"
        myLikeCountLabel.text = String(feed.likeCount!)
        checkLike = feed.isLike ?? false
        
        if checkLike == true {
            myLikeBtn.tintColor = yumyumYellow.toColor()
        } else {
            myLikeBtn.tintColor = .white
        }
        
    }
    
}
