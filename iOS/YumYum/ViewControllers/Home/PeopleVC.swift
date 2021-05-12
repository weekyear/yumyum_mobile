//
//  PeopleVC.swift
//  YumYum
//
//  Created by 염성훈 on 2021/05/13.
//

import UIKit

class PeopleVC: UIViewController {
    let cellIdentifire : String  = "peopleCell"
    var peopleFeedList: [Feed] = []
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var ProfileImgView: UIImageView!
    
    @IBOutlet weak var IntroduceLabel : UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
}

extension PeopleVC: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.peopleFeedList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell: PeopleCell = collectionView.dequeueReusableCell(withReuseIdentifier: self.cellIdentifire, for: indexPath) as! PeopleCell
        
        return cell
    }
    
    
}
