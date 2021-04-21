//
//  ReviewVC.swift
//  YumYum
//
//  Created by Ahyeonway on 2021/04/17.
//


import UIKit
import AVFoundation
import PhotosUI


class CameraVC: UIViewController {


    var captureSession:AVCaptureSession = AVCaptureSession()
    var videoDevice: AVCaptureDevice!
    
    var videoInput: AVCaptureDeviceInput!
    
    var audioInput: AVCaptureDeviceInput!
    
    var videoOutput: AVCaptureMovieFileOutput!
    


    @IBOutlet weak var cameraView: UIView!
    
    lazy var previewLayer = AVCaptureVideoPreviewLayer(session: self.captureSession)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        
        cameraView.layer.addSublayer(previewLayer)
        requestCameraPermission()
        requestGalleryPermission()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if !captureSession.isRunning {
            captureSession.startRunning()
        }
    }
    
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        // 세션 클로징
        captureSession.stopRunning()
        
    }
    
    
    private func requestCameraPermission() {
        // camera 권한 설정
        switch AVCaptureDevice.authorizationStatus(for: .video) {
            case .authorized: // 이전에 카메라 권한 설정 허용해놨을 경우
                print("설정 옛날에 해쮜")
                setupCaptureSession()
                
            case .notDetermined: // 카메라 권한 설정 안해놨을 경우
                AVCaptureDevice.requestAccess(for: .video) { granted in
                    if granted {
                        print("Camera: 권한 허용")
                    } else {
                        print("Camera: 권한 거부")
                    }
                }
                
            case .denied: // The user has previously denied access.
                return
                
            case .restricted: // The user can't grant access due to restrictions.
                return
            default:
                break
        }
    }
    
    private func setupCaptureSession() {
        
        // 1. captureSession 생성
//        captureSession = AVCaptureSession()
        captureSession.sessionPreset = .high
        
        // 2. captureDevice 생성
        let videoDevice = bestDevice(in: .back)
        
        // 3. captureDeviceInput 생성
        do {
            // 3-1 세션 구성의 시작
            captureSession.beginConfiguration()
            
            // 3-2 비디오 장치에 대한 입력을 세션에 추가
            let videoInput = try AVCaptureDeviceInput(device: videoDevice)
            if captureSession.canAddInput(videoInput) {
                captureSession.addInput(videoInput)
            }
            
            // 3-3 오디오 장치에 대한 입력을 세션에 추가
            let audioDevice = AVCaptureDevice.default(for: .audio)!
            let audioInput = try AVCaptureDeviceInput(device: audioDevice)
            if captureSession.canAddInput(audioInput) {
                captureSession.addInput(audioInput)
            }
            
            // 3-4 파일로 출력하기 위한 인스턴스
            videoOutput = AVCaptureMovieFileOutput()
            
            if captureSession.canAddOutput(videoOutput) {
                captureSession.addOutput(videoOutput)
            }
        
            // 3-5 세션 구성의 완료
            captureSession.commitConfiguration()
            
        }
            
        catch let error as NSError {
            NSLog("\(error), \(error.localizedDescription)")
        }
        
            
    }
    
    
    
    private func tempURL() -> URL? {
      let directory = NSTemporaryDirectory() as NSString
      
      if directory != "" {
        let path = directory.appendingPathComponent(NSUUID().uuidString + ".mp4")
        return URL(fileURLWithPath: path)
      }
      
      return nil
    }
    
    

    
    // device 별 카메라 사용 가능한 타입
    func bestDevice(in position: AVCaptureDevice.Position) -> AVCaptureDevice {
        var deviceTypes: [AVCaptureDevice.DeviceType]!
        
        if #available(iOS 11.1, *) {
            deviceTypes = [.builtInTrueDepthCamera, .builtInDualCamera, .builtInWideAngleCamera]
        }
        else {
            deviceTypes = [.builtInDualCamera, .builtInWideAngleCamera]
        }
        
        let discoverySession = AVCaptureDevice.DiscoverySession(
            deviceTypes: deviceTypes,
            mediaType: .video,
            position: .unspecified
        )
        
        let devices = discoverySession.devices
        
        guard !devices.isEmpty else { fatalError("Missing capture devices.")}
        return devices.first(where: { device in device.position == position })!
        
    }
    
    
    // gallery 권한 설정
    func requestGalleryPermission() {
        PHPhotoLibrary.requestAuthorization({status in
            switch status {
            case .authorized:
                print("Gallery: 권한 허용")
            case .notDetermined, .restricted:
                print("Gallery: 선택하지 않음")
            case .denied:
                print("Gallery: 선택하지 않음")
            default:
                break
            }
        })
    }
    
    
    
    
}


extension CameraVC: AVCaptureFileOutputRecordingDelegate {
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        
    }
    
    
}
