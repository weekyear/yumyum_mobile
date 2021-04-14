package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dao.FileDao;
import com.yumyum.domain.feed.entity.File;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileDao fileDao;

    private static final String SAVE_PATH = "/var/www/html/dist/single/";
    private static final String PREFIX_URL = "https://i4b101.p.ssafy.io/single/";

    private static final String WINDOWS_SAVE_PATH = FileSystemView.getFileSystemView().getHomeDirectory().toString() + "/single/";
    private static final String WINDOWS_PREFIX_URL = FileSystemView.getFileSystemView().getHomeDirectory().toString() + "/single/";


    public String upload(MultipartFile multipartFile) {
        String videoUrl;
        String thumbnailUrl;

        try {
            // 파일 정보
            String originFilename = multipartFile.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
            Long size = multipartFile.getSize();

            // 서버에서 저장 할 파일 이름
            StringBuilder savedFileNameBuilder = genSaveFileName();
            String savedFileName = savedFileNameBuilder.toString();
            String savedVideoName = savedFileNameBuilder.append(extName).toString();
//            System.out.println("originFilename : " + originFilename);
//            System.out.println("extensionName : " + extName);
//            System.out.println("size : " + size);
//            System.out.println("saveFileName : " + savedVideoName);
            String videoPath = writeFile(multipartFile, savedVideoName);

//            thumbnailUrl = createThumnail(videoPath, savedFileName);

            if (System.getProperty("os.name").contains("Windows")) {
                videoUrl = WINDOWS_PREFIX_URL + savedVideoName;
            } else {
                videoUrl = PREFIX_URL + savedVideoName;
            }
//            System.out.println("url : " + videoUrl);

            saveFile(originFilename, savedVideoName, videoUrl, extName);
        }
        catch (IOException e) {
            // 원래라면 RuntimeException 을 상속받은 예외가 처리되어야 하지만
            // 편의상 RuntimeException을 던진다.
            // throw new FileUploadException();
            throw new RuntimeException(e);
        }
        return videoUrl;
    }


    // 현재 시간을 기준으로 파일 이름 생성
    private StringBuilder genSaveFileName() {
        StringBuilder fileName = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        fileName.append(calendar.get(Calendar.YEAR));
        fileName.append(calendar.get(Calendar.MONTH));
        fileName.append(calendar.get(Calendar.DATE));
        fileName.append(calendar.get(Calendar.HOUR));
        fileName.append(calendar.get(Calendar.MINUTE));
        fileName.append(calendar.get(Calendar.SECOND));
        fileName.append(calendar.get(Calendar.MILLISECOND));

        return fileName;
    }


    // 파일을 실제로 write 하는 메서드
    private String writeFile(MultipartFile multipartFile, String saveFileName) throws IOException{
        byte[] data = multipartFile.getBytes();
        FileOutputStream fos;
        String videoPath;
        if (System.getProperty("os.name").contains("Windows")) {
            videoPath = WINDOWS_SAVE_PATH + saveFileName;
        } else {
            videoPath = SAVE_PATH + saveFileName;
        }
        fos = new FileOutputStream(videoPath);
        fos.write(data);
        fos.close();
        return videoPath;
    }

    public String createThumnail(String videoPath, String savedFileName) {
        int frameNumber = 0;

        java.io.File video = new java.io.File(videoPath);
        String thumbnailPath;
        if (System.getProperty("os.name").contains("Windows")) {
            thumbnailPath = WINDOWS_SAVE_PATH + savedFileName + "_thumbnail.png";
        } else {
            thumbnailPath = PREFIX_URL + savedFileName + "_thumbnail.png";
        }
        java.io.File thumbnail = new java.io.File(thumbnailPath);

        try {
            Picture picture = FrameGrab.getFrameFromFile(video, frameNumber);
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", thumbnail);
            return thumbnailPath;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JCodecException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @Transactional
    String saveFile(String origFileName, String fileName, String filePath, String extensionName) {
        File file = File.builder()
                .origFileName(origFileName)
                .fileName(fileName)
                .filePath(filePath)
                .extensionName(extensionName)
                .build();
        return fileDao.save(file).getFilePath();
    }

    @Transactional
    public File getFile(long id) {
        return fileDao.findById(id).get();
    }
}
