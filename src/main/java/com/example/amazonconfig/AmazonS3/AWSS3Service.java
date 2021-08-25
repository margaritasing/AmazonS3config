package com.example.amazonconfig.AmazonS3;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AWSS3Service {

    void uploadFile(MultipartFile file);


    List<String> getObjectFromS3();


    InputStream downloadFile(String key);
}
