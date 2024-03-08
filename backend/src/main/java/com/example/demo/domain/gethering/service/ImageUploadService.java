package com.example.demo.domain.gethering.service;


import com.example.demo.domain.gethering.repository.ImageRepository;
import com.example.demo.domain.util.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ImageUploadService {

    private final ImageUpload imageUpload;
    private final ImageRepository imageRepository;
}