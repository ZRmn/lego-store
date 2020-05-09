package com.zrmn.web.controllers;

import com.zrmn.model.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/download")
public class DownloadController
{
    @Autowired
    private FileStorageService fileStorageService;

//    @GetMapping("/{filePath:.+}")
//    public ResponseEntity download(@PathVariable String filePath, HttpServletRequest request) throws IOException
//    {
//        Resource resource = fileStorageService.loadAsResource(filePath);
//        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//
//        if(contentType == null)
//        {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION)
//                .body(resource);
//    }
}
