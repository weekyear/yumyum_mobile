package com.yumyum.domain.feed.application;

import com.yumyum.domain.feed.dto.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceTest {

    private final FileUploadService fileUploadService;
    private final FileThumbnailService fileThumbnailService;

    public FileDto uploadFeedMedia(final MultipartFile file){
        String videoPath = fileUploadService.upload(file);
        String thumbnailPath = fileThumbnailService.createThumbnail(videoPath);
        return new FileDto(videoPath, thumbnailPath);
    }
}
