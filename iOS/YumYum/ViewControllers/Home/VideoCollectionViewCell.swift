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
    
    @IBOutlet weak var likeImgView: UIImageView!
    
    var player : AVPlayer?
    
    let userData = UserDefaults.getLoginedUserInfo()
    
    var checkLike: Bool = false

    override func awakeFromNib() {
        super.awakeFromNib()
        
    }
    
    public func configureVideo(with feed:Feed, myLikeFeed:Feed) {
        player = AVPlayer(url: feed.videoPath!)
        let playerView = AVPlayerLayer()
        playerView.player = player
        playerView.frame = videoLayout.bounds
        playerView.videoGravity = .resize
        videoLayout.layer.addSublayer(playerView)
        player?.volume = 0
        player?.play()
        bringUpViewobject()
        loadData(feed: feed, myLikeFeed: myLikeFeed)
    }
    
    private func loadData(feed:Feed, myLikeFeed: Feed) {
        foodLabel.text = feed.title
        userLabel.text = feed.user?.nickname
        reviewLabel.text = feed.content
        placeLabel.text = feed.place?.name
        addressLabel.text = feed.place?.address
        likeCountLabel.text = String(feed.likeCount!)
        checkLike = myLikeFeed.isLike!
        
        if checkLike == true {
            let image = UIImage(named: "ic_thumbs_up_filled")
            likeImgView.image = image
        } else {
            let image = UIImage(named: "ic_thumbs_up")
            likeImgView.image = image
        }
        
    }
    
    private func bringUpViewobject() {
        videoLayout.bringSubviewToFront(foodLabel)
        videoLayout.bringSubviewToFront(placeLabel)
        videoLayout.bringSubviewToFront(addressLabel)
        videoLayout.bringSubviewToFront(reviewLabel)
        videoLayout.bringSubviewToFront(userLabel)
        videoLayout.bringSubviewToFront(placeStackView)
        videoLayout.bringSubviewToFront(likeImgView)
        videoLayout
            .bringSubviewToFront(likeCountLabel)
    }
}

