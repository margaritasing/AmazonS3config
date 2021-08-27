package com.example.amazonconfig.AmazonS3;

import alkemy.challenge.Challenge.Alkemy.service.AWSS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class AmazonController {


    public final AWSS3Service awss3Service;
    public final MessageSource messageSource;

    @Autowired
    public AmazonController(AWSS3Service awss3Service, MessageSource messageSource) {
        this.awss3Service = awss3Service;
        this.messageSource = messageSource;
    }


    @PostMapping(value = "/upload")
    public ResponseEntity<String> upLoadFile(@RequestPart(value = "file")MultipartFile file){
       try {
           awss3Service.uploadFile(file);
           String response = messageSource.getMessage("uploaded.file",new String[]{"AWSS3"}, LocaleContextHolder.getLocale());
           return new ResponseEntity<>(response, HttpStatus.OK);
       }catch (Exception e){
           String notFoundMsg = messageSource.getMessage("no.records.found",new String[]{"AWSS3"}, LocaleContextHolder.getLocale());
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMsg);
       }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> listFiles() {
        List<String> amazon = awss3Service.getObjectFromS3();
        if (amazon.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("no.records.found", new Object[]{"AWSS3"}, LocaleContextHolder.getLocale()));
        } return ResponseEntity.status(HttpStatus.OK).body(amazon);

    }


    @GetMapping(value="/download")
    public ResponseEntity<Resource> download(@RequestParam (value = "key", required = false) String key){
        InputStreamResource resource = new InputStreamResource(awss3Service.downloadFile(key));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\""+ key +"\"").body(resource);
    }


}
