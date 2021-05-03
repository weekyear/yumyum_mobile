//
//  VideoPlayBackVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import UIKit
import  AVFoundation

class VideoPlayBackVC: UIViewController {
    
    static func instance(videoUrl: URL) -> VideoPlayBackVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "VideoPlayBackVC") as! VideoPlayBackVC
        vc.videoUrl = videoUrl
        return vc
    }
    
    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!

    var videoUrl: URL!
    
    @IBOutlet weak var videoView: UIView!

    @IBAction func didTapPrevButton(_ sender: Any) {
        print("도로마무는 불가능합니다 고갱님")
//        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func didTapNextButton(_ sender: Any) {
        let vc = ReviewVC.instance(videoUrl: videoUrl)
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setLayout()
        playBackVideo()
        
        
    }
    func setLayout() {
        self.navigationController?.navigationBar.isHidden = true
    }
    
    func playBackVideo() {
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        view.layoutIfNeeded()
        let playerItem = AVPlayerItem(url: videoUrl as URL)
        avPlayer.replaceCurrentItem(with: playerItem)
        avPlayer.play()
    }

}
