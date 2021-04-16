//
//  VideoCollectionViewCell.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/16.
//

// videocell 한개이고 비디오 한 화면에 담기는 것들을 제어한다.
import UIKit
import AVFoundation

class VideoCollectionViewCell: UICollectionViewCell {
    
    static let identifier = "VideoCollectionViewCell"
    // SUbviews
    var player : AVPlayer?
    
    
    override init(frame:CGRect){
        super.init(frame:frame)
    }
    
    public func configure(with model: VideoModel){
        contentView.backgroundColor = .red
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

}
