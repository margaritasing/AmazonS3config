package com.example.amazonconfig.AmazonS3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class AmazonController {

    public final AWSS3ServiceImpl awss3service;


    @Autowired
    public AmazonController(AWSS3ServiceImpl awss3service) {
        this.awss3service = awss3service;
    }


    @PostMapping(value = "/upload")
    public ResponseEntity<String> upLoadFile(@RequestPart(value = "file")MultipartFile file){
        awss3service.uploadFile(file);
        String response = "El archivo "+file.getOriginalFilename()+"fue cargado correctamente a S3";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> listFiles(){
        return new ResponseEntity<>(awss3service.getObjectFromS3(), HttpStatus.OK);
    }

    @GetMapping(value="/download")
    public ResponseEntity<Resource> download(@RequestParam("Key") String key){
        InputStreamResource resource = new InputStreamResource(awss3service.downloadFile(key));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\""+key+"\"").body(resource);
    }


}
