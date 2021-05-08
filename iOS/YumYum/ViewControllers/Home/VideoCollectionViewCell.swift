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
    
    @IBOutlet weak var likeImgView: UIImageView!
    
    var player : AVPlayer?
    
    private var model : VideoVO?
    
    let userData = UserDefaults.getLoginedUserInfo()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    public func configureVideo(with feed:Feed){
        print(feed)
        foodLabel.text = feed.title
        userLabel.text = userData!["nickname"].stringValue
        reviewLabel.text = feed.content
        player = AVPlayer(url: feed.videoPath!)
        let playerView = AVPlayerLayer()
        playerView.player = player
        playerView.frame = videoLayout.bounds
        playerView.videoGravity = .resize
        videoLayout.layer.addSublayer(playerView)
        player?.volume = 0
        player?.play()
        videoLayout.bringSubviewToFront(foodLabel)
        videoLayout.bringSubviewToFront(placeLabel)
        videoLayout.bringSubviewToFront(addressLabel)
        videoLayout.bringSubviewToFront(reviewLabel)
        videoLayout.bringSubviewToFront(userLabel)
        videoLayout.bringSubviewToFront(placeStackView)
        videoLayout.bringSubviewToFront(likeImgView)
    }
}

