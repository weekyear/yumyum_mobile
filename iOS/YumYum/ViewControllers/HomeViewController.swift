//
//  HomeViewController.swift
//  YumYum
//
//  Created by 염성훈 on 2021/04/15.
//

import UIKit

// 화면에 담길 Label을 모델로 만든다.
struct VideoModel{
    let caption : String
    let userName : String
    let placeName : String
    let addressName : String
    let videoFileName : String
    let videoFileFormat : String
    let review : String
}

class HomeViewController : UIViewController{
    // 접근 제어
    private var collectionView: UICollectionView?
    
    private var data = [VideoModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for _ in 0..<10 {
            let model = VideoModel(caption: "delicious chickhen", userName: "Yeom", placeName: "YumYumChickhen", addressName: "대전시 레자미 3차 802호", videoFileName: "video", videoFileFormat: "mp4", review: "노맛!!!!")
            
            data.append(model)
        }
        
        // layout 개겣를 생성하고
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        // video 한개의 크기와 범위를 정해준다.
        layout.itemSize = CGSize(width: view.frame.size.width, height: view.frame.size.height)
        // 레이아웃에 마진주기
        layout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        // 콜랙션 뷰의 객체를 frame 위치에 생성한다. frame : .zero 가로, 세로 0 좌표는 x:0 ,y :0 과 같다.
        collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
        // 셀을 콜렉션 뷰에 등록한다.
        collectionView?.register(VideoCollectionViewCell.self, forCellWithReuseIdentifier: VideoCollectionViewCell.identifier)
        // 페이징 적용
        collectionView?.isPagingEnabled = true
        // 콜렉션 뷰를 사용하기 위해서는 반드시 dataSource를 delegate로 지정해줘함.
        collectionView?.dataSource = self
        view.addSubview(collectionView!)
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        collectionView?.frame = view.bounds
    }
}

extension HomeViewController: UICollectionViewDataSource{

    // 지정된 섹션에 표시할 항목의 갯수를 묻는 매서드.(필수)
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return data.count
    }
    // 콜렉션 뷰의 특정 인덱스에서 표시할 셀을 요청하는 메서드이다.(필수)
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let model = data[indexPath.row]
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: VideoCollectionViewCell.identifier, for: indexPath) as! VideoCollectionViewCell
        
        cell.configure(with: model)
        
        return cell
    }
        
}
