package com.zrmn.model.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileStorageService
{
    void store(String filePath, MultipartFile file);

    File load(String filePath);

    Resource loadAsResource(String filePath);
}
