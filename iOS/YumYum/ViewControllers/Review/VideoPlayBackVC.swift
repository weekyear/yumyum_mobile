//
//  VideoPlayBackVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/28.
//

import UIKit
import  AVFoundation

class VideoPlayBackVC: UIViewController {
    
    static func instance() -> VideoPlayBackVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "VideoPlayBackVC") as! VideoPlayBackVC
        return vc
    }
    
    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!

    var videoURL: URL!
    //connect this to your uiview in storyboard
    @IBOutlet weak var videoView: UIView!

    
    @IBAction func didTapPrevButton(_ sender: Any) {
        print("되돌아가기")
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func didTapNextButton(_ sender: Any) {
        print("녹화끝")
        let vc = ReviewVC.instance()
        print(vc)
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationController?.navigationBar.isHidden = true
        
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)

        view.layoutIfNeeded()

        let playerItem = AVPlayerItem(url: videoURL as URL)
        avPlayer.replaceCurrentItem(with: playerItem)

        avPlayer.play()

    }
    
    func setLayout() {
        
    }
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
