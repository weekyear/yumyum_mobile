//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/20.
//

import UIKit
import AVFoundation

class VideoCollectionViewCell: UICollectionViewCell {

    
    @IBOutlet weak var videoLayout: UIView!
    
    @IBOutlet weak var foodLabel: UILabel!
    
    @IBOutlet weak var placeLabel: UILabel!
    
    @IBOutlet weak var addressLabel: UILabel!
    
    @IBOutlet weak var reviewLabel: UILabel!
    
    @IBOutlet weak var userLabel: UILabel!
    
    @IBOutlet weak var placeStackView : UIStackView!
    
    var player : AVPlayer?
    
    private var model : VideoVO?
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    // 외부에서 configure 함수를 참조할 수 있게 구성한다. HomeViewController에서 참조하게 된다.
    public func configure(with model:VideoVO){
        // 현재 위에서 지정한 모델 변수를 viewController에서 보내준 model에는 데이터가 담겨 있다.
        self.model = model
        foodLabel.text = model.foodTitle
        placeLabel.text = model.placeName
        addressLabel.text = model.addressName
        reviewLabel.text = model.review
        userLabel.text = model.userName
        configuareVideo()
    }
    
    
    private func configuareVideo(){
        guard let model = model else {
                  return
        }
        guard let path = Bundle.main.path(forResource: model.videoFileName, ofType: model.videoFileFormat) else {
                    print("파일을 찾지 못했어요!")
                    return
                }
        
        player = AVPlayer(url: URL(fileURLWithPath: path))
        
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
    }

}

