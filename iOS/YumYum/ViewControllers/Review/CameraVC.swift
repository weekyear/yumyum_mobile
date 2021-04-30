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
    
    static func instance() -> CameraVC {
        let vc = UIStoryboard.init(name: "Review", bundle: nil).instantiateViewController(withIdentifier: "CameraVC") as! CameraVC
        return vc
    }

    var captureSession:AVCaptureSession = AVCaptureSession()
    var videoDevice: AVCaptureDevice!
    var videoInput: AVCaptureDeviceInput!
    var audioInput: AVCaptureDeviceInput!
    var videoOutput: AVCaptureMovieFileOutput!
    var outputUrl: URL!
    private let sessionQueue = DispatchQueue(label: "session queue")
    private var backgroundRecordingID: UIBackgroundTaskIdentifier?
    
    @IBOutlet weak var cameraView: PreviewView!
    @IBOutlet weak var recordButton: UIButton!
    
    @IBAction func closeCamera(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    var previewLayer: AVCaptureVideoPreviewLayer!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // layout
        setupLayout()
    
        // 권한 설정
        requestCameraPermission()
        requestGalleryPermission()
        
        resetTimer()

    }
    
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

    }
    
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        print("camera", #function)
        
        // 세션 클로징
        captureSession.stopRunning()
        // 타이머 해제
        timer?.invalidate()
        timer = nil
        
    }
    
    func setupLayout() {
        // recordButton
        recordButton.layer.cornerRadius = 50
        recordButton.layer.borderWidth = 10
        recordButton.layer.borderColor = UIColor.yellow.cgColor
        
        // navbar
        self.navigationController?.navigationBar.transparentNavigationBar()
    }
    
    private func requestCameraPermission() {
        // camera 권한 설정
        switch AVCaptureDevice.authorizationStatus(for: .video) {
            case .authorized: // 이전에 카메라 권한 설정 허용해놨을 경우
                print("설정 옛날에 해쮜")
                setupCamera()
                
            case .notDetermined: // 카메라 권한 설정 안해놨을 경우
                AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                    if granted {
                        print("Camera: 권한 허용")
                        DispatchQueue.main.async {
                            self?.setupCamera()
                        }
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
    
    private func setupCamera() {
        if !captureSession.isRunning {
            captureSession.startRunning()
        }
        
        cameraView.session = captureSession
        cameraView.videoPreviewLayer.videoGravity = .resizeAspectFill
        
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
            
            
            if let connection = self.videoOutput?.connection(with: .video) {
                if connection.isVideoStabilizationSupported {
                    connection.preferredVideoStabilizationMode = .auto
                }
            }
            
        
            // 3-5 세션 구성의 완료
            captureSession.commitConfiguration()
            
        }
            
        catch let error as NSError {
            NSLog("\(error), \(error.localizedDescription)")
        }
        
            
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
    
    // Mark: Record Video
    @IBAction func didTapRecordVideo(_ sender: Any) {
        
        print("녹화중")
        guard let movieFileOutput = self.videoOutput else {
            return
        }
        
        // 타이머
        guard timer == nil else { return }
        resetTimer()
        
        let videoPreviewLayerOrientation = cameraView.videoPreviewLayer.connection?.videoOrientation
        
        sessionQueue.async { [self] in
            if !movieFileOutput.isRecording {
                if UIDevice.current.isMultitaskingSupported {
                    self.backgroundRecordingID = UIApplication.shared.beginBackgroundTask(expirationHandler: nil)
                }
                
                // Update the orientation on the movie file output video connection before recording.
                let movieFileOutputConnection = movieFileOutput.connection(with: .video)
                movieFileOutputConnection?.videoOrientation = videoPreviewLayerOrientation!
                
                let availableVideoCodecTypes = movieFileOutput.availableVideoCodecTypes
                
                if availableVideoCodecTypes.contains(.hevc) {
                    movieFileOutput.setOutputSettings([AVVideoCodecKey: AVVideoCodecType.h264], for: movieFileOutputConnection!)
                }
                
                // Start recording video to a temporary file.
                let outputFileName = NSUUID().uuidString
                let outputFilePath = (NSTemporaryDirectory() as NSString).appendingPathComponent((outputFileName as NSString).appendingPathExtension("mp4")!)
                
                movieFileOutput.startRecording(to: URL(fileURLWithPath: outputFilePath), recordingDelegate: self)
                
                DispatchQueue.main.async {
                    // 타이머 생성
                    timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { (timer) in
                            guard timer.isValid else { return }
                        self.updateTimer(timer: timer, videoOutput: movieFileOutput)
                    })
                    timer?.tolerance = 0.2
                }
                
                
            } else {
                movieFileOutput.stopRecording()
            }
            
        }
        
    }
    
    
    // Mark: Timer
    
    var timer: Timer?
    var leftTime: Int = 3
    @IBOutlet weak var timeLabel: UILabel!
    
    
    func resetTimer() {
        leftTime = 3
        timeLabel.text = "3"
    }
    
    func updateTimer(timer: Timer, videoOutput: AVCaptureMovieFileOutput) {
        print(#function)
        leftTime -= 1
        timeLabel.text = "\(leftTime)"
        if leftTime <= 0 {
            timer.invalidate()
            videoOutput.stopRecording()
        }
    }
    
    
}


extension CameraVC: AVCaptureFileOutputRecordingDelegate {
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        
        print("ooutput: \(outputFileURL)")
        
        func cleanup() {
            let path = outputFileURL.path
            if FileManager.default.fileExists(atPath: path) {
                do {
                    try FileManager.default.removeItem(atPath: path)
                } catch {
                    print("Could not remove file at url: \(outputFileURL)")
                }
            }
            
            if let currentBackgroundRecordingID = backgroundRecordingID {
                backgroundRecordingID = UIBackgroundTaskIdentifier.invalid
                
                if currentBackgroundRecordingID != UIBackgroundTaskIdentifier.invalid {
                    UIApplication.shared.endBackgroundTask(currentBackgroundRecordingID)
                }
            }
        }
        
        var success = true
        
        if error != nil {
            print("Movie file finishing error: \(String(describing: error))")
            success = (((error! as NSError).userInfo[AVErrorRecordingSuccessfullyFinishedKey] as AnyObject).boolValue)!
        }
        
        if success {
            // Check the authorization status.
            PHPhotoLibrary.requestAuthorization { status in
                if status == .authorized {
                    // Save the movie file to the photo library and cleanup.
                    PHPhotoLibrary.shared().performChanges({
                        let options = PHAssetResourceCreationOptions()
                        options.shouldMoveFile = true
                        let creationRequest = PHAssetCreationRequest.forAsset()
                        creationRequest.addResource(with: .video, fileURL: outputFileURL, options: options)
                    }, completionHandler: { success, error in
                        if !success {
                            print("AVCam couldn't save the movie to your photo library: \(String(describing: error))")
                        }
                        cleanup()
                    }
                    )
                } else {
                    cleanup()
                }
            }
        } else {
            cleanup()
        }
        print("녹화끝")
        let vc = VideoPlayBackVC.instance()
        print(vc)
        vc.videoURL = outputFileURL
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    
}



//private func tempURL() -> URL? {
//  let directory = NSTemporaryDirectory() as NSString
//
//  if directory != "" {
//    let path = directory.appendingPathComponent(NSUUID().uuidString + ".mp4")
//    return URL(fileURLWithPath: path)
//  }
//
//  return nil
//}
