package com.yumyum.domain.feed.application;

import lombok.RequiredArgsConstructor;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class FileThumbnailService {

    public String createThumbnail(String videoPath) { // 동영상에서 첫 프레임을 썸네일로 추출 및 저장
        int frameNumber = 0;

        File video = new File(videoPath);
        String thumbnailPath;
        thumbnailPath = videoPath + "_thumbnail.png";
        File thumbnail = new File(thumbnailPath);

        try {
            System.out.println(video.getPath());
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
}
