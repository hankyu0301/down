package com.example.demo.domain.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageUpload {

    @Value("${file.upload.path}")
    private String uploadPath;

    public String saveImage(Long getheringId, MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException("파일명이 없습니다.");
        }

        String saveFileName = UUID.randomUUID() + "_" + getFileExtension(fileName);
        String savePath = uploadPath + "/gethering/" + getheringId + "/" + saveFileName;

        try {
            file.transferTo(new File(savePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveFileName;
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("파일명에 확장자가 없습니다.");
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}